package portfolio.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import portfolio.StudentManagement.controller.converter.StudentConverter;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;
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

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    // 新規受講生情報を登録する処理を実装する。
    // コース情報も一緒に登録できるように実装する。コースは単体で良い。
    service.registerStudent(studentDetail);
    return "redirect:/studentList";
  }

}
