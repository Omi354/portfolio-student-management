package portfolio.StudentManagement.service;

import java.util.List;
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

  public StudentDetail searchForStudentDetailById(String id) {
    Student studentSearchById = studentRepository.selectStudentById(id);
    List<StudentCourse> studentCourseListSearchById = studentCourseRepository.selectStudentCourseListByStudentId(
        id);
    StudentDetail studentDetailSearchById = new StudentDetail();

    studentDetailSearchById.setStudent(studentSearchById);
    studentDetailSearchById.setStudentCourseList(studentCourseListSearchById);
    return studentDetailSearchById;
  }

  public List<Student> searchForAllStudentList() {
    return studentRepository.selectAllStudentList();
  }

  public List<StudentCourse> searchForAllStudentCourseList() {
    return studentCourseRepository.selectAllStudentCourseList();
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
    Student studentAfterModifying = studentDetail.getStudent();
    List<StudentCourse> studentCourseListAfterModifying = studentDetail.getStudentCourseList();

    studentRepository.updateStudent(studentAfterModifying);
    for (StudentCourse studentCourse : studentCourseListAfterModifying) {
      studentCourseRepository.updateStudentCourse(studentCourse);
    }
  }
}
