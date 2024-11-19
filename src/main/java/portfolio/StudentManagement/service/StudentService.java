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
   * 受講生コース情報の一覧検索を行います。全件取得のため条件指定は行いません。
   *
   * @return 受講生コース一覧（全件）
   */
  public List<StudentCourse> getAllStudentCourseList() {
    return studentCourseRepository.selectAllCourseList();
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student newStudent = studentDetail.getStudent();
    StudentCourse newStudentCourse = studentDetail.getStudentCourseList().getFirst();

    newStudent.setId(UUID.randomUUID().toString());
    newStudent.setRemark("");
    newStudent.setIsDeleted(false);

    newStudentCourse.setId(UUID.randomUUID().toString());
    newStudentCourse.setStudentId(newStudent.getId());
    newStudentCourse.setStartDate(LocalDateTime.now());
    newStudentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    studentRepository.createStudent(newStudent);
    studentCourseRepository.createStudentCourse(newStudentCourse);
    return studentDetail;
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
