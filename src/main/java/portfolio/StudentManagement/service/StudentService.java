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
    List<Student> allStudentList = studentRepository.selectAllStudentList();
    List<Student> studentListOrMore30YearsOld = allStudentList.stream()
        .filter(student -> student.getAge() >= 30)
        .toList();

    return studentListOrMore30YearsOld;
  }

  public List<StudentCourse> searchForAllStudentCourseList() {
    List<StudentCourse> allStudentCourseList = studentCourseRepository.selectAllStudentCourseList();
    List<StudentCourse> javaCourseList = allStudentCourseList.stream()
        .filter(studentCourse -> studentCourse.getCourseName().contains("Java"))
        .toList();

    return javaCourseList;
  }
}
