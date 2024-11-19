package portfolio.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです
 */
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
   * 受講生検索です IDに紐づく任意の受講生の情報を取得します
   *
   * @param id 受講生id
   * @return idに紐づく任意の受講生情報
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable String id) {
    return service.getStudentDetailById(id);
  }


  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
    StudentDetail registeredStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(registeredStudentDetail);
  }


  @PatchMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新に成功しました");
  }

}
