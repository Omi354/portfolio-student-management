package portfolio.StudentManagement.data;


import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnrollmentStatus {

  private String id;
  private String studentCourseId;
  private Status status;
  private LocalDateTime createdAt;

  @Getter
  public enum Status {
    PENDING("仮申込"),
    APPROVED("本申込"),
    IN_PROGRESS("受講中"),
    COMPLETED("受講終了");

    private final String japanese;

    Status(String japanese) {
      this.japanese = japanese;
    }
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
