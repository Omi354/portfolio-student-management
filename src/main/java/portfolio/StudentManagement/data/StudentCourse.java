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

  @Schema(description = "ID、UUIDを自動付与", example = "78af6312-a2cd-11ef-b71f-6845f15f510c")
  private String id;

  @Schema(description = "受講生ID、外部キー", example = "5998fd5d-a2cd-11ef-b71f-6845f15f510c")
  private String studentId;

  @Schema(description = "受講コース名", example = "Javaフルコース")
  @NotBlank
  private String courseName;

  @Schema(description = "受講開始日", example = "2024-01-10T00:00:00")
  private LocalDateTime startDate;

  @Schema(description = "受講修了予定日", example = "2025-01-10T00:00:00")
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
