package portfolio.StudentManagement.data;


import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String id;
  private String fullName;
  private String kana;
  private String nickName;
  private String email;
  private String city;
  private int age;
  private Gender gender;
  private String remark;
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
