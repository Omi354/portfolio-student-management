package portfolio.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
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

  public List<Student> searchForAllStudentList() {
    return studentRepository.selectAllStudentList();
  }

  public List<StudentCourse> searchForAllStudentCourseList() {
    return studentCourseRepository.selectAllStudentCourseList();
  }
}
