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
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.exception.EnrollmentStatusBadRequestException;
import portfolio.StudentManagement.exception.EnrollmentStatusNotFoundException;
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
   * 受講生詳細一覧の全件検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧（全件）
   */
  @Operation(
      summary = "受講生詳細一覧の検索",
      description = "受講生詳細の一覧を検索します。全件検索を行うので、条件指定は行いません。",
      responses = {@ApiResponse(
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class))
          )
      )}
  )
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.getAllStudentDetailList();
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
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @Pattern(
      regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$")
  String id)
      throws StudentNotFoundException {
    return service.getStudentDetailById(id);
  }

  /**
   * 申込状況のステータスを指定して受講生詳細を検索します。
   *
   * @param status 申込状況のステータス
   * @return 指定したステータスの受講生詳細
   */
  @GetMapping("/studentListWithStatus")
  public List<StudentDetail> getStudentDetailListByStatus(@RequestParam Status status) {
    return service.getStudentDetailListByStatus(status);
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
          description = "新規登録したい受講生詳細　※受講生のid,remark,isDeleted、受講生コース情報のid,studentId,startDate,endDateは自動付与されるためリクエストボディに含まれません",
          required = true,
          content = @Content(
              schema = @Schema(implementation = StudentDetail.class)
          )
      )
  )
  @PostMapping("/registerStudent")
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
          description = "更新したい受講生詳細",
          required = true,
          content = @Content(
              schema = @Schema(implementation = StudentDetail.class)
          )
      )
  )
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail)
      throws StudentNotFoundException, StudentCourseNotFoundException {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新に成功しました");
  }

  /**
   * 申込状況を更新します。 後ろに戻るような更新の場合や、受講生コース情報と正しく紐づいていない場合にはエラーをなげます
   *
   * @param enrollmentStatus
   * @return 処理結果
   * @throws EnrollmentStatusNotFoundException   受講生コースと正しく紐づいていない場合の例外処理
   * @throws EnrollmentStatusBadRequestException 後ろに戻るような更新の場合の例外処理
   */
  @PostMapping("/updateStatus")
  public ResponseEntity<String> updateEnrollmentStatus(
      @RequestBody EnrollmentStatus enrollmentStatus)
      throws EnrollmentStatusNotFoundException, EnrollmentStatusBadRequestException {
    service.updateEnrollmentStatus(enrollmentStatus);
    return ResponseEntity.ok("ステータスの更新に成功しました");
  }

}
