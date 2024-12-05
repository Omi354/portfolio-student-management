package portfolio.StudentManagement.data;


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
public class EnrollmentStatus {

  private String id;
  private String studentCourseId;
  private Status status;
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
