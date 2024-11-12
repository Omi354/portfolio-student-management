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
  public List<StudentDetail> getStudentDetailsList(List<Student> allStudentList,
      List<StudentCourse> allStudentCourseList) {
    List<StudentDetail> studentDetailsList = new ArrayList<>();
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
