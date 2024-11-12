package portfolio.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.StudentManagement.controller.converter.StudentConverter;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.service.StudentService;

@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public List<Student> getStudentList() {
    return service.searchForAllStudentList();
  }

  @GetMapping("/studentCourseList")
  public List<StudentCourse> getStudentCourseList() {
    return service.searchForAllStudentCourseList();
  }

  @GetMapping("/studentDetailList")
  public List<StudentDetail> getStudentDetailsList() {
    List<Student> allStudentList = service.searchForAllStudentList();
    List<StudentCourse> allStudentCourseList = service.searchForAllStudentCourseList();

    return converter.getStudentDetailsList(allStudentList, allStudentCourseList);
  }

}
