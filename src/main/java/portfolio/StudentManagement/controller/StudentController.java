package portfolio.StudentManagement.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;
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
    return service.searchForAllStudentList();
  }

  @GetMapping("/studentCourseList")
  public List<StudentCourse> getStudentCourseList() {
    return service.searchForAllStudentCourseList();
  }

  @GetMapping("/studentDetailList")
  public List<StudentDetail> getStudentDetailsList() {
    List<StudentDetail> studentDetailsList = new ArrayList<>();
    List<Student> allStudentList = service.searchForAllStudentList();
    List<StudentCourse> allStudentCourseList = service.searchForAllStudentCourseList();

    for (Student student : allStudentList) {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertedStudentCoursesList = new ArrayList<>();
      for (StudentCourse studentCourse : allStudentCourseList) {
        if (student.getId().equals(studentCourse.getStudentId())) {
          convertedStudentCoursesList.add(studentCourse);
        }
      }
      studentDetail.setStudentCourseList(convertedStudentCoursesList);
      studentDetailsList.add(studentDetail);
    }
    return studentDetailsList;

    // { student:{Studentの情報},StudentCourse: {StudentCourse},{StudentCourse} }
  }
}
