package portfolio.StudentManagement.data;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private String id;
  private String studentId;
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StudentCourse that = (StudentCourse) o;
    return Objects.equals(id, that.id) && Objects.equals(studentId,
        that.studentId) && Objects.equals(courseName, that.courseName)
        && Objects.equals(startDate, that.startDate) && Objects.equals(endDate,
        that.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, studentId, courseName, startDate, endDate);
  }
}
