package portfolio.StudentManagement.controller;

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
import org.springframework.web.bind.annotation.RestController;
import portfolio.StudentManagement.domain.StudentDetail;
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
   * @param service 　受講生サービス
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
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @Pattern(
      regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
      message = "UUIDの形式が誤っています") String id)
      throws StudentNotFoundException {
    return service.getStudentDetailById(id);
  }

  /**
   * 受講生詳細を新規登録します。
   *
   * @param studentDetail 受講生詳細
   * @return 処理結果
   */
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
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail)
      throws StudentNotFoundException, StudentCourseNotFoundException {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新に成功しました");
  }

}
