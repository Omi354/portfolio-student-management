package portfolio.StudentManagement.data;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  @Schema(description = "ID、UUIDを自動付与", example = "5998fd5d-a2cd-11ef-b71f-6845f15f510c")
  private String id;

  @Schema(description = "氏名", example = "山田 太郎")
  @NotBlank(message = "入力が必要です")
  private String fullName;

  @Schema(description = "フリガナ、カタカナと半角・全角スペースのみ許可", example = "ヤマダ　タロウ")
  @Pattern(
      regexp = "^[ァ-ヶー\\s　]+$",
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
    Male, Female, Non_binary
  }

  /**
   * 受講生に初期値を設定します。 IDに自動生成されたUUID、備考に空欄、キャンセルフラグにfalseを設定します。
   *
   * @param newStudent 新規受講生
   */
  public static void initStudent(Student newStudent) {
    newStudent.setId(UUID.randomUUID().toString());
    newStudent.setRemark("");
    newStudent.setIsDeleted(false);
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
