package portfolio.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.service.StudentService;

@RestController
public class StudentController {
  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/studentList")
  public List<Student> getStudentList() {
    return service.selectAllStudentList();
  }

  @GetMapping("/studentCourseList")
  public List<StudentCourse> getStudentCourseList() {
    return service.gelectAllStudentCourseList();
  }
}
