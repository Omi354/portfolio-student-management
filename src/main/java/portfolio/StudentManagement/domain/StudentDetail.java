package portfolio.StudentManagement.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentCourseList;
}
