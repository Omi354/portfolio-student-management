package portfolio.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.service.StudentService;

@Component
public class StudentConverter {
  private StudentService service;

  @Autowired
  public StudentConverter(StudentService service) {
    this.service = service;
  }

  public List<StudentDetail> convertStudentDetails() {
    List<StudentDetail> studentDetailsList = new ArrayList<>();
    List<Student> allStudentList = service.searchForAllStudentList();
    List<StudentCourse> allStudentCourseList = service.searchForAllStudentCourseList();

    allStudentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertedStudentCoursesList = allStudentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());
      studentDetail.setStudentCourseList(convertedStudentCoursesList);
      studentDetailsList.add(studentDetail);
    });
    return studentDetailsList;
  }
}
