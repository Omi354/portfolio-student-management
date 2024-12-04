package portfolio.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Schema(description = "受講生コース情報")
@Getter
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

  @Schema(description = "コース申込状況のオブジェクト")
  private EnrollmentStatus enrollmentStatus;

  /**
   * 受講生コース情報のコンストラクターです。 UUIDを自動生成し、それ以外のフィールドについては受講生コースビルダーから情報を受け取ります。
   *
   * @param builder 受講生コースビルダー
   */
  public StudentCourse(StudentCourseBuilder builder) {
    this.id = UUID.randomUUID().toString();
    this.studentId = builder.studentId;
    this.courseName = builder.courseName;
    this.startDate = builder.startDate;
    this.endDate = builder.endDate;
    this.enrollmentStatus = builder.enrollmentStatus;
  }

  /**
   * IDを指定してインスタンス生成する際に使用するコンストラクターです。
   *
   * @param id      ID
   * @param builder 受講生コースビルダー
   */
  public StudentCourse(String id, StudentCourseBuilder builder) {
    this.id = id;
    this.studentId = builder.studentId;
    this.courseName = builder.courseName;
    this.startDate = builder.startDate;
    this.endDate = builder.endDate;
    this.enrollmentStatus = builder.enrollmentStatus;
  }

  /**
   * 受講生コースビルダー（内部クラス）です。受講生コース情報のインスタンス生成時にフィールドの値を設定する役割を持ちます。
   */
  @Schema(description = "受講生コースビルダー")
  public static class StudentCourseBuilder {

    @Schema(description = "受講生ID、外部キー", example = "5998fd5d-a2cd-11ef-b71f-6845f15f510c")
    private String studentId;

    @Schema(description = "受講コース名", example = "Javaフルコース")
    @NotBlank
    private String courseName;

    @Schema(description = "受講開始日", example = "2024-01-10T00:00:00")
    private LocalDateTime startDate = LocalDateTime.now();

    @Schema(description = "受講修了予定日", example = "2025-01-10T00:00:00")
    private LocalDateTime endDate = LocalDateTime.now().plusYears(1);

    @Schema(description = "コース申込状況のオブジェクト")
    private EnrollmentStatus enrollmentStatus = null;

    /**
     * 受講生コースビルダーのコンストラクターです。 必須の受講生IDと受講コース名を引数に取り、それ以外のフィールドは別途メソッドから設定できます。
     *
     * @param studentId  受講生ID
     * @param courseName コース名
     */
    public StudentCourseBuilder(String studentId, String courseName) {
      this.studentId = studentId;
      this.courseName = courseName;
    }

    public StudentCourseBuilder enrollmentStatus(EnrollmentStatus enrollmentStatus) {
      this.enrollmentStatus = enrollmentStatus;
      return this;
    }

    public StudentCourseBuilder startDate(LocalDateTime startDate) {
      this.startDate = startDate;
      return this;
    }

    public StudentCourseBuilder endDate(LocalDateTime endDate) {
      this.endDate = endDate;
      return this;
    }

    public StudentCourse build() {
      return new StudentCourse(this);
    }

    public StudentCourse buildWithId(String id) {
      return new StudentCourse(id, this);
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
