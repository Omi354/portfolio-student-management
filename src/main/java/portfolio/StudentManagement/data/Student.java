package portfolio.StudentManagement.data;


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

  public enum Gender {
    Male, Female, Non_binary
  }

}
