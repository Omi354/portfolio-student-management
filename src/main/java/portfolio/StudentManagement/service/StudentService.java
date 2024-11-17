package portfolio.StudentManagement.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.repository.StudentCourseRepository;
import portfolio.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository studentRepository;
  private StudentCourseRepository studentCourseRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository,
      StudentCourseRepository studentCourseRepository) {
    this.studentRepository = studentRepository;
    this.studentCourseRepository = studentCourseRepository;
  }

  public StudentDetail getStudentDetailById(String id) {
    Student student = studentRepository.selectStudentById(id);
    List<StudentCourse> studentCourseList = studentCourseRepository.selectCourseListByStudentId(id);
    StudentDetail studentDetail = new StudentDetail();

    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentCourseList);
    return studentDetail;
  }

  public List<Student> getAllStudentList() {
    return studentRepository.selectAllStudentList();
  }

  public List<StudentCourse> getAllStudentCourseList() {
    return studentCourseRepository.selectAllCourseList();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    Student newStudent = studentDetail.getStudent();
    StudentCourse newStudentCourse = studentDetail.getStudentCourseList().getFirst();
    studentRepository.createStudent(newStudent);
    studentCourseRepository.createStudentCourse(newStudent, newStudentCourse);
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
