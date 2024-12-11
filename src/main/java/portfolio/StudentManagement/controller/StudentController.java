package portfolio.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import portfolio.StudentManagement.data.EnrollmentStatus;
import portfolio.StudentManagement.data.EnrollmentStatus.Status;
import portfolio.StudentManagement.data.ErrorResponse;
import portfolio.StudentManagement.data.Student.Gender;
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.exception.EnrollmentStatusBadRequestException;
import portfolio.StudentManagement.exception.EnrollmentStatusNotFoundException;
import portfolio.StudentManagement.exception.InvalidRequestException;
import portfolio.StudentManagement.exception.StudentCourseNotFoundException;
import portfolio.StudentManagement.exception.StudentNotFoundException;
import portfolio.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  /**
   * コンストラクタ
   *
   * @param service 受講生サービス
   */
  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細検索。クエリパラメータを使用しない場合は全件検索を行います。 statusパラメータとして申込状況を指定すると、該当する申込状況の受講生詳細を検索します。
   *
   * @param status 申込状況のステータス（例: "仮申込", "本申込", "受講中", "受講終了"）、非必須
   * @return 受講生詳細のリスト
   */
  @Operation(
      summary = "受講生詳細の検索",
      description = "受講生詳細の一覧を検索します。クエリパラメータ `status` を指定しない場合、全件検索を行います。"
          +
          "クエリパラメータ `status` を指定すると、そのステータスに該当する受講生詳細を検索します。",
      parameters = {
          @Parameter(
              name = "status",
              description = "申込状況のステータス（例: \"仮申込\", \"本申込\", \"受講中\", \"受講終了\"）。指定しない場合、全件を返します。",
              required = false,
              schema = @Schema(implementation = Status.class)
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "検索結果としての受講生詳細のリスト",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class))
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "不正なクエリパラメータ",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )
      }
  )
  @GetMapping("/students")
  public List<StudentDetail> getStudentList(@RequestParam(required = false) Status status,
      @RequestParam(required = false) String fullName,
      @RequestParam(required = false) String kana,
      @RequestParam(required = false) String nickName,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) Integer minAge,
      @RequestParam(required = false) Integer maxAge,
      @RequestParam(required = false) Gender gender,
      @RequestParam(required = false) String remark) throws InvalidRequestException {

    if (status != null && (Stream.of(fullName, kana, nickName, email, city, minAge, maxAge, gender,
            remark)
        .anyMatch(Objects::nonNull))) {
      throw new InvalidRequestException(
          "申込状況とその他の検索条件を同時に指定することは出来ません");
    }
    if (status != null) {
      return service.getStudentDetailListByStatus(status);
    }

    if ((maxAge != null && minAge != null && minAge > maxAge) ||
        (maxAge != null && maxAge < 0) ||
        (minAge != null && minAge < 0)) {
      throw new InvalidRequestException(
          "minAgeとmaxAgeの指定が無効です: 範囲が逆、または負の値が指定されています");
    }
    return service.getStudentDetailList(fullName, kana, nickName, email, city, minAge, maxAge,
        gender, remark);
  }

  /**
   * 受講生検索です IDに紐づく任意の受講生の情報を取得します。 IDに紐づく受講生が存在しない場合エラーを発生させます。
   *
   * @param id 受講生id
   * @return idに紐づく任意の受講生情報
   * @throws StudentNotFoundException 受講生が存在しない場合の例外処理
   */
  @Operation(
      summary = "受講生検索",
      description = "パスで指定されたIDに該当する受講生を検索します。IDに紐づく受講生が存在しない場合エラーを発生させます。",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )),
          @ApiResponse(
              responseCode = "404", description = "指定されたIDの受講生が存在しない場合のエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "UUIDの形式が誤っていた際のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )
      },
      parameters = {
          @Parameter(in = ParameterIn.PATH, name = "id",
              required = true,
              description = "受講生ID",
              schema = @Schema(
                  type = "string",
                  format = "uuid",
                  description = "自動生成されたUUID",
                  example = "5998fd5d-a2cd-11ef-b71f-6845f15f510c"
              )
          )}
  )
  @GetMapping("/students/{id}")
  public StudentDetail getStudent(@PathVariable @Pattern(
      regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$")
  String id)
      throws StudentNotFoundException {
    return service.getStudentDetailById(id);
  }

  /**
   * 受講生詳細を新規登録します。
   *
   * @param studentDetail 受講生詳細
   * @return 処理結果
   */
  @Operation(
      summary = "受講生登録",
      description = "受講生を登録します",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "リクエストボディのバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )
      },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "新規登録したい受講生詳細　※受講生のid,remark,isDeleted、受講生コース情報のid,studentId,startDate,endDate、申込状況のid,studentCourseId,cratedAtは自動付与されるためリクエストボディに含まれません",
          required = true,
          content = @Content(
              schema = @Schema(implementation = StudentDetail.class)
          )
      )
  )
  @PostMapping("/students")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail registeredStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(registeredStudentDetail);
  }


  /**
   * 受講生詳細を更新します。
   *
   * @param studentDetail 受講生詳細
   * @return 処理結果
   * @throws StudentNotFoundException       受講生が存在しない場合の例外処理
   * @throws StudentCourseNotFoundException 　受講生コース情報が存在しない場合の例外処理
   */
  @Operation(
      summary = "受講生更新",
      description = "受講生の情報を更新します。指定した受講生情報や受講生コース情報が存在しない場合、エラーを発生させます。",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "text/plain;charset=UTF-8",
                  schema = @Schema(
                      type = "string",
                      example = "更新に成功しました"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "リクエストボディのバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404", description = "指定されたIDの受講生または受講生コース情報が存在しない場合のエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          ),
      },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "更新したい受講生詳細※受講生コース情報にネストされている申込状況については指定できません、別途\"/enrollment-status\"を使用して更新します",
          required = true,
          content = @Content(
              schema = @Schema(implementation = StudentDetail.class)
          )
      )
  )
  @PutMapping("/students")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail)
      throws StudentNotFoundException, StudentCourseNotFoundException {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新に成功しました");
  }


  /**
   * 申込状況を更新します。 後ろに戻るような更新の場合や、受講生コース情報と正しく紐づいていない場合にはエラーをなげます
   *
   * @param enrollmentStatus 　申込状況
   * @return 処理結果
   * @throws EnrollmentStatusNotFoundException   受講生コースと正しく紐づいていない場合の例外処理
   * @throws EnrollmentStatusBadRequestException 後ろに戻るような更新の場合の例外処理
   */
  @Operation(
      summary = "申込状況更新",
      description = "申込状況を更新します。実際には同じ受講生コース情報に紐づく申込状況を作成し、過去の申込状況をDB上に保持できるようにしています。",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "ok",
              content = @Content(
                  mediaType = "text/plain;charset=UTF-8",
                  schema = @Schema(
                      type = "string",
                      example = "更新に成功しました"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400", description = "申込状況のステータスが後ろに戻るようなリクエストの場合に発生する例外処理",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404", description = "リクエストの申込状況オブジェクトが受講生コースと正しく紐づいていない場合の例外処理",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )
      },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "更新したい申込状況　※id, createdAt は自動付与されるためリクエストボディに含まれません",
          required = true,
          content = @Content(
              schema = @Schema(implementation = EnrollmentStatus.class)
          )
      ))
  @PostMapping("/students/courses/enrollment-status")
  public ResponseEntity<String> updateEnrollmentStatus(
      @RequestBody EnrollmentStatus enrollmentStatus)
      throws EnrollmentStatusNotFoundException, EnrollmentStatusBadRequestException {
    service.updateEnrollmentStatus(enrollmentStatus);
    return ResponseEntity.ok("ステータスの更新に成功しました");
  }

}
