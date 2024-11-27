package portfolio.StudentManagement.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.StudentManagement.controller.converter.StudentConverter;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.Student.Gender;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.exception.StudentCourseNotFoundException;
import portfolio.StudentManagement.exception.StudentNotFoundException;
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
  public StudentDetail getStudentDetailById(String id) throws StudentNotFoundException {
    Student student = studentRepository.selectStudentById(id);
    // 指定したID該当する受講生が存在しない場合、エラーを発生させます
    if (student == null) {
      throw new StudentNotFoundException();
    }

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
    Student receivedStudent = studentDetail.getStudent();
    StudentCourse newStudentCourse = studentDetail.getStudentCourseList().getFirst();

    String fullName = receivedStudent.getFullName();
    String kana = receivedStudent.getKana();
    String nickName = receivedStudent.getNickName();
    String email = receivedStudent.getEmail();
    String city = receivedStudent.getCity();
    int age = receivedStudent.getAge();
    Gender gender = receivedStudent.getGender();

    Student newStudent = new Student.StudentBuilder(fullName, email, city, age)
        .kana(kana).nickName(nickName).gender(gender).build();

    studentRepository.createStudent(newStudent);
    studentCourseRepository.createStudentCourse(newStudentCourse);
    return studentDetail;
  }

  /**
   * 受講生情報と受講生コース情報をそれぞれ更新します。
   * 更新処理前に受け取った受講生情報・受講生コース情報と、登録されている受講生情報・受講生コース情報を比較し、修正点が有る場合のみ処理を実行します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail)
      throws StudentNotFoundException, StudentCourseNotFoundException {
    // リクエストとして受け取った受講生と受講生コース情報を定義します
    Student receivedStudent = studentDetail.getStudent();
    List<StudentCourse> receivedStudentCourseList = studentDetail.getStudentCourseList();

    // リクエストとして受け取った受講生IDから、現時点でDBに登録されている受講生と受講生コース情報を定義します
    String studentId = receivedStudent.getId();
    Student currentStudent = studentRepository.selectStudentById(studentId);
    List<StudentCourse> currentStudentCourseList = studentCourseRepository
        .selectCourseListByStudentId(studentId);

    // リクエストとして受け取った受講生IDに該当する受講生が存在しない場合にエラーを発生させます
    if (currentStudent == null) {
      throw new StudentNotFoundException();
    }

    // リクエストとして受け取った受講生情報・受講生コース情報とDBに登録されている受講生・受講生コース情報に差異がある場合に更新処理を実行します
    updateStudentIfModified(receivedStudent, currentStudent);
    updateStudentCourseIfModified(receivedStudentCourseList, currentStudentCourseList);
  }

  /**
   * リクエストとして受け取った受講生コース情報と現時点でDBに登録されている受講生コース情報に差異がある場合に更新処理を実行します。
   * 受講生コース情報はリスト渡されるため、ループを回して中身の受講生コース情報を取り出して処理します。
   *
   * @param receivedStudentCourseList リクエストとして受け取った受講生コース
   * @param currentStudentCourseList  現時点でDBに登録されている受講生コース情報
   */
  void updateStudentCourseIfModified(List<StudentCourse> receivedStudentCourseList,
      List<StudentCourse> currentStudentCourseList) throws StudentCourseNotFoundException {

    // currentStudentCourseList　を　{"id", {studentCourse}} のマップに変換します
    Map<String, StudentCourse> currentStudentCourseMap = currentStudentCourseList.stream()
        .collect(
            Collectors.toMap(StudentCourse::getId, studentCourse -> studentCourse, (a, b) -> b));
    // receivedStudentCourseListをfor文で回し、中身のreceivedStudentCourseを一つずつ取り出します
    for (StudentCourse receivedStudentCourse : receivedStudentCourseList) {
      // receivedStudentCourseのIDに紐づく、現時点で登録されている受講生コース情報を取得します
      StudentCourse currentStudentCourse = currentStudentCourseMap
          .get(receivedStudentCourse.getId());
      // receivedStudentCourseのIDに紐づく受講生コース情報が存在しない場合、エラーを発生させます
      if (currentStudentCourse == null) {
        throw new StudentCourseNotFoundException();
      }
      // 現時点で登録されているコース名と、受け取ったコース名が異なる場合にRepositoryにidと変更後のコース名を渡します
      if (!receivedStudentCourse.equals(currentStudentCourse)) {
        studentCourseRepository.updateStudentCourse(receivedStudentCourse);
      }
    }
  }

  /**
   * リクエストとして受け取った受講生情報と現時点でDBに登録されている受講生情報に差異がある場合に更新処理を実行します。
   *
   * @param receivedStudent リクエストとして受け取った受講生情報
   * @param currentStudent  現時点でDBに登録されている受講生情報
   */
  void updateStudentIfModified(Student receivedStudent, Student currentStudent) {
    if (!receivedStudent.equals(currentStudent)) {
      studentRepository.updateStudent(receivedStudent);
    }
  }
}
