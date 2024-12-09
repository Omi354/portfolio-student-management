package portfolio.StudentManagement.data;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "申込状況")
public class EnrollmentStatus {

  @Schema(description = "ID", example = "e05baab7-a353-4b00-a7ed-5d47bd12eb65")
  private String id;

  @Schema(description = "受講生コースID、外部キー", example = "78af6312-a2cd-11ef-b71f-6845f15f510c")
  private String studentCourseId;

  @NotNull
  @Schema(description = "申込状況", example = "受講中")
  private Status status;

  @Schema(description = "レコード作成日", example = "2024-01-10T00:00:00")
  private LocalDateTime createdAt;

  public enum Status {
    仮申込, 本申込, 受講中, 受講終了;

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnrollmentStatus that = (EnrollmentStatus) o;
    return Objects.equals(id, that.id) && Objects.equals(studentCourseId,
        that.studentCourseId) && status == that.status && Objects.equals(createdAt,
        that.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, studentCourseId, status, createdAt);
  }
}
