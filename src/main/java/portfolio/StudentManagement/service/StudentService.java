package portfolio.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.StudentManagement.controller.converter.StudentConverter;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.repository.StudentCourseRepository;
import portfolio.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository studentRepository;
  private StudentCourseRepository studentCourseRepository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository studentRepository,
      StudentCourseRepository studentCourseRepository, StudentConverter converter) {
    this.studentRepository = studentRepository;
    this.studentCourseRepository = studentCourseRepository;
    this.converter = converter;
  }

  /**
   * 受講生検索です。 IDに紐づく任意の受講生の情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細（受講生と受講コース情報）
   */
  public StudentDetail getStudentDetailById(String id) {
    Student student = studentRepository.selectStudentById(id);
    List<StudentCourse> studentCourseList = studentCourseRepository.selectCourseListByStudentId(id);
    return new StudentDetail(student, studentCourseList);
  }

  /**
   * 受講生詳細の一覧検索を行います。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> getAllStudentDetailList() {
    List<Student> studentList = studentRepository.selectAllStudentList();
    List<StudentCourse> studentCourseList = studentCourseRepository.selectAllCourseList();

    return converter.getStudentDetailsList(studentList, studentCourseList);
  }

  /**
   * 受講生情報と受講生コース情報をそれぞれ登録します。 登録前に受講生情報については、デフォルト値としてUUIDをランダム, 備考を空欄、キャンセルフラグをfalseに設定します。
   * 登録前に受講生コース情報については、デフォルト値としてUUIDをランダム、受講生IDを同時に作成される受講生情報のID、受講開始日をレコード登録日時、受講修了予定日を受講開始日から１年後に設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 新規登録された受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student newStudent = studentDetail.getStudent();
    String newStudentId = newStudent.getId();
    StudentCourse newStudentCourse = studentDetail.getStudentCourseList().getFirst();

    initStudent(newStudent);
    initStudentCourse(newStudentCourse, newStudentId);

    studentRepository.createStudent(newStudent);
    studentCourseRepository.createStudentCourse(newStudentCourse);
    return studentDetail;
  }

  /**
   * 受講生コース情報に初期値を設定します。
   * IDに自動生成されたUUID、受講生IDに同時に作成される受講生のID、受講開始日にレコードが作成された時点の日時、受講修了予定日にレコードが作成された時点から１年後の日時を設定します。
   *
   * @param newStudentCourse 新規受講生コース情報
   * @param newStudentId     新規受講生ID
   */
  private static void initStudentCourse(StudentCourse newStudentCourse, String newStudentId) {
    newStudentCourse.setId(UUID.randomUUID().toString());
    newStudentCourse.setStudentId(newStudentId);
    newStudentCourse.setStartDate(LocalDateTime.now());
    newStudentCourse.setEndDate(LocalDateTime.now().plusYears(1));
  }

  /**
   * 受講生に初期値を設定します。 IDに自動生成されたUUID、備考に空欄、キャンセルフラグにfalseを設定します。
   *
   * @param newStudent 新規受講生
   */
  private static void initStudent(Student newStudent) {
    newStudent.setId(UUID.randomUUID().toString());
    newStudent.setRemark("");
    newStudent.setIsDeleted(false);
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    Student modifiedStudent = studentDetail.getStudent();
    List<StudentCourse> modifiedStudentCourseList = studentDetail.getStudentCourseList();

    String studentId = modifiedStudent.getId();
    Student currentStudent = studentRepository.selectStudentById(studentId);
    List<StudentCourse> currentStudentCourseList = studentCourseRepository.selectCourseListByStudentId(
        studentId);

    if (!modifiedStudent.equals(currentStudent)) {
      studentRepository.updateStudent(modifiedStudent);
    }

    Map<String, StudentCourse> currentStudentCourseMap = currentStudentCourseList.stream()
        .collect(
            Collectors.toMap(StudentCourse::getId, currentCourse -> currentCourse, (a, b) -> b));

    for (StudentCourse modifiedStudentCourse : modifiedStudentCourseList) {
      StudentCourse currentStudentCourse = currentStudentCourseMap.get(
          modifiedStudentCourse.getId());
      if (!modifiedStudentCourse.equals(currentStudentCourse)) {
        studentCourseRepository.updateStudentCourse(modifiedStudentCourse);
      }
    }
  }
}
