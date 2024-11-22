package portfolio.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  private String id;
  private String studentId;
  @NotBlank
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  /**
   * 受講生コース情報に初期値を設定します。
   * IDに自動生成されたUUID、受講生IDに同時に作成される受講生のID、受講開始日にレコードが作成された時点の日時、受講修了予定日にレコードが作成された時点から１年後の日時を設定します。
   *
   * @param newStudentCourse 新規受講生コース情報
   * @param newStudentId     新規受講生ID
   */
  public static void initStudentCourse(StudentCourse newStudentCourse, String newStudentId) {
    newStudentCourse.setId(UUID.randomUUID().toString());
    newStudentCourse.setStudentId(newStudentId);
    newStudentCourse.setStartDate(LocalDateTime.now());
    newStudentCourse.setEndDate(LocalDateTime.now().plusYears(1));
  }

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
