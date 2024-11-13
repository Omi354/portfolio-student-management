package portfolio.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import portfolio.StudentManagement.controller.converter.StudentConverter;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> allStudentList = service.searchForAllStudentList();
    List<StudentCourse> allStudentCourseList = service.searchForAllStudentCourseList();
    model.addAttribute("studentList", // これはテンプレートリテラルに渡す変数名
        converter.getStudentDetailsList(allStudentList, allStudentCourseList));

    return "studentList"; // これはテンプレートエンジンのファイル名
  }

  @GetMapping("/studentCourseList")
  public String getStudentCourseList(Model model) {
    model.addAttribute("allStudentCourseList", service.searchForAllStudentCourseList());
    return "studentCourseList";
  }

}
