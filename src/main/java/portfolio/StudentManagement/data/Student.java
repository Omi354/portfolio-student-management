package portfolio.StudentManagement.data;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@Schema(description = "受講生")
@Getter
public class Student {

  @Schema(description = "ID、UUIDを自動付与", example = "5998fd5d-a2cd-11ef-b71f-6845f15f510c")
  private String id;

  @Schema(description = "氏名", example = "山田 太郎")
  @NotBlank(message = "入力が必要です")
  private String fullName;

  @Schema(description = "フリガナ、カタカナと半角・全角スペースのみ許可", example = "ヤマダ　タロウ")
  @Pattern(
      regexp = "^$|^[ァ-ヶー\\s　]+$", // 空文字またはカタカナとスペース
      message = "カタカナとスペースのみを入力してください"
  )
  private String kana;

  @Schema(description = "ニックネーム", example = "たろ")
  private String nickName;

  @Schema(description = "メールアドレス", example = "yamada@example.com")
  @NotBlank(message = "入力が必要です")
  @Email(message = "メールアドレスの形式が誤っています")
  private String email;

  @Schema(description = "居住地域、都道府県＋市区町村までを想定", example = "東京都港区")
  @NotBlank(message = "入力が必要です")
  private String city;

  @Schema(description = "年齢、0~150までを許可", example = "32")
  @Range(min = 0, max = 150, message = "正しい値を入力してください")
  private int age;

  @Schema(description = "性別", example = "Male")
  private Gender gender;

  @Schema(description = "備考", example = "入院のため利用休止中")
  private String remark;

  @Schema(description = "削除フラグ", example = "false")
  private Boolean isDeleted;

  public enum Gender {
    Male, Female, NON_BINARY, Unspecified
  }

  /**
   * 受講生のコンストラクターです。 IDのみUUIDを自動付与し、それ以外のフィールドはStudentBuilder経由でインスタンス生成を行います。
   *
   * @param builder 受講生ビルダー
   */
  private Student(StudentBuilder builder) {
    this.id = UUID.randomUUID().toString();
    this.fullName = builder.fullName;
    this.kana = builder.kana;
    this.nickName = builder.nickName;
    this.email = builder.email;
    this.city = builder.city;
    this.age = builder.age;
    this.gender = builder.gender;
    this.remark = builder.remark;
    this.isDeleted = builder.isDeleted;
  }

  /**
   * テスト専用のコンストラクターです
   *
   * @param id      ID
   * @param builder 受講生ビルダー
   */
  private Student(String id, StudentBuilder builder) {
    this.id = id;
    this.fullName = builder.fullName;
    this.kana = builder.kana;
    this.nickName = builder.nickName;
    this.email = builder.email;
    this.city = builder.city;
    this.age = builder.age;
    this.gender = builder.gender;
    this.remark = builder.remark;
    this.isDeleted = builder.isDeleted;
  }

  /**
   * 受講生ビルダーです。
   */
  public static class StudentBuilder {

    @Schema(description = "氏名", example = "山田 太郎")
    private String fullName;

    @Schema(description = "メールアドレス", example = "yamada@example.com")
    private String email;

    @Schema(description = "居住地域、都道府県＋市区町村までを想定", example = "東京都港区")
    private String city;

    @Schema(description = "年齢、0~150までを許可", example = "32")
    private int age;

    @Schema(description = "フリガナ、カタカナと半角・全角スペースのみ許可", example = "ヤマダ　タロウ")
    private String kana = "";

    @Schema(description = "ニックネーム", example = "たろ")
    private String nickName = "";

    @Schema(description = "性別", example = "Male")
    private Gender gender = Gender.Unspecified;

    @Schema(description = "備考", example = "入院のため利用休止中")
    private String remark = "";

    @Schema(description = "削除フラグ", example = "false")
    private Boolean isDeleted = false;

    /**
     * 受講生ビルダーのコンストラクターです。 必須情報のフルネーム、メールアドレス、地域、年齢のみ引数に取ります。 それ以外のフィールドについては必要に応じてメソッドから指定します。
     *
     * @param fullName フルネーム
     * @param email    メールアドレス
     * @param city     地域
     * @param age      年齢
     */
    public StudentBuilder(String fullName, String email, String city, int age) {
      this.fullName = fullName;
      this.email = email;
      this.city = city;
      this.age = age;
    }

    public StudentBuilder kana(String kana) {
      this.kana = kana;
      return this;
    }

    public StudentBuilder nickName(String nickName) {
      this.nickName = nickName;
      return this;
    }

    public StudentBuilder gender(Gender gender) {
      this.gender = gender;
      return this;
    }

    public StudentBuilder remark(String remark) {
      this.remark = remark;
      return this;
    }

    public StudentBuilder isDeleted(Boolean isDeleted) {
      this.isDeleted = isDeleted;
      return this;
    }

    public Student build() {
      return new Student(this);
    }

    public Student useOnlyTestBuildWithId(String id) {
      return new Student(id, this);
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
    Student student = (Student) o;
    return age == student.age && Objects.equals(id, student.id) && Objects.equals(
        fullName, student.fullName) && Objects.equals(kana, student.kana)
        && Objects.equals(nickName, student.nickName) && Objects.equals(email,
        student.email) && Objects.equals(city, student.city) && gender == student.gender
        && Objects.equals(remark, student.remark) && Objects.equals(isDeleted,
        student.isDeleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, kana, nickName, email, city, age, gender, remark, isDeleted);
  }
}
