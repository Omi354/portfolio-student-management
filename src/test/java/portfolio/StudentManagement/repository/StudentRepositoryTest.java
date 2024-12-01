package portfolio.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.Student.Gender;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;


  @Test
  void 受講生全件検索_削除フラグがfalseの受講生情報を全件取得できること() {
    // 準備
    String id1 = "1c91a1b0-1111-1111-1111-111111111111";
    String id2 = "2c92b2c0-2222-2222-2222-222222222222";
    String id3 = "3c93c3d0-3333-3333-3333-333333333333";
    String id4 = "4c94d4e0-4444-4444-4444-444444444444";
    String id5 = "5c95e5f0-5555-5555-5555-555555555555";

    // 実行
    List<Student> actual = sut.selectAllStudentList();

    // 検証
    assertThat(actual.size()).isEqualTo(5);
    assertThat(actual.get(0).getId()).isEqualTo(id1);
    assertThat(actual.get(1).getId()).isEqualTo(id2);
    assertThat(actual.get(2).getId()).isEqualTo(id3);
    assertThat(actual.get(3).getId()).isEqualTo(id4);
    assertThat(actual.get(4).getId()).isEqualTo(id5);
  }

  @Test
  void 受講生検索_存在しないIDが渡された場合_Nullが返ってくること() {
    // 準備
    String id = UUID.randomUUID().toString();

    // 実行
    Student actual = sut.selectStudentById(id);

    // 検証
    assertThat(actual).isNull();
  }

  @ParameterizedTest
  @CsvSource({
      "'1c91a1b0-1111-1111-1111-111111111111', '佐藤 太郎', 'サトウ タロウ', 'たろちゃん', 'taro.sato@example.com', '東京', 25, 'Male', '優秀な学生です', false",
      "'2c92b2c0-2222-2222-2222-222222222222', '鈴木 花子', 'スズキ ハナコ', 'はなちゃん', 'hanako.suzuki@example.com', '大阪', 28, 'Female', 'クリエイティブ志向', false",
      "'3c93c3d0-3333-3333-3333-333333333333', '高橋 健一', 'タカハシ ケンイチ', NULL, 'kenichi.takahashi@example.com', '名古屋', 30, 'Male', 'リーダーシップあり', false",
      "'4c94d4e0-4444-4444-4444-444444444444', '田中 美香', 'タナカ ミカ', 'みかりん', 'mika.tanaka@example.com', '福岡', 22, 'Female', '向上心が強い', false",
      "'5c95e5f0-5555-5555-5555-555555555555', '山本 大輔', 'ヤマモト ダイスケ', NULL, 'daisuke.yamamoto@example.com', '札幌', 27, 'Male', '努力家', false",
      "'6c96f6g0-6666-6666-6666-666666666666', '伊藤 次郎', 'イトウ ジロウ', 'ジロちゃん', 'jiro.ito@example.com', '仙台', 35, 'Male', '退会済みの学生', true"
  })
  void 受講生検索_適切なIDが渡された場合_IDに紐づく学生の情報を取得できること(String id,
      String fullName, String kana, String nickName,
      String email, String city, int age, String gender,
      String remark, boolean isDeleted) {

    // 準備
    String actualNickName = nickName.equals("NULL") ? null : nickName;

    Gender actualGender = switch (gender) {
      case "Male" -> Gender.Male;
      case "Female" -> Gender.Female;
      case "NON_BINARY" -> Gender.NON_BINARY;
      case "Unspecified" -> Gender.Unspecified;
      default -> throw new IllegalStateException("Unexpected value: " + gender);
    };

    // 実行
    Student actual = sut.selectStudentById(id);

    // 検証
    assertThat(actual.getId()).isEqualTo(id);
    assertThat(actual.getFullName()).isEqualTo(fullName);
    assertThat(actual.getKana()).isEqualTo(kana);
    assertThat(actual.getNickName()).isEqualTo(actualNickName);
    assertThat(actual.getEmail()).isEqualTo(email);
    assertThat(actual.getCity()).isEqualTo(city);
    assertThat(actual.getAge()).isEqualTo(age);
    assertThat(actual.getGender()).isEqualTo(actualGender);
    assertThat(actual.getRemark()).isEqualTo(remark);
    assertThat(actual.getIsDeleted()).isEqualTo(isDeleted);
  }

  @ParameterizedTest
  @MethodSource("provideNewStudents")
  void 受講生登録_渡されたStudentオブジェクトのレコードがDBにINSERTされること(Student student) {
    // 準備
    String id = student.getId();
    int recordCountBefore = sut.selectAllStudentList().size();

    // 実行
    sut.createStudent(student);
    Student actual = sut.selectStudentById(id);
    int recordCountAfter = sut.selectAllStudentList().size();

    //検証
    assertThat(actual.getId()).isEqualTo(student.getId());
    assertThat(actual.getFullName()).isEqualTo(student.getFullName());
    assertThat(actual.getKana()).isEqualTo(student.getKana());
    assertThat(actual.getNickName()).isEqualTo(student.getNickName());
    assertThat(actual.getEmail()).isEqualTo(student.getEmail());
    assertThat(actual.getCity()).isEqualTo(student.getCity());
    assertThat(actual.getAge()).isEqualTo(student.getAge());
    assertThat(actual.getGender()).isEqualTo(student.getGender());
    assertThat(actual.getRemark()).isEqualTo(student.getRemark());
    assertThat(actual.getIsDeleted()).isEqualTo(student.getIsDeleted());
    assertThat(recordCountAfter).isEqualTo(recordCountBefore + 1);
  }

  public static Stream<Student> provideNewStudents() {
    return Stream.of(
        new Student.StudentBuilder("中村 一郎", "ichiro.nakamura@example.com", "横浜", 29)
            .kana("ナカムラ イチロウ")
            .nickName("いっちゃん")
            .gender(Student.Gender.Male)
            .remark("優秀な開発者")
            .build(),

        new Student.StudentBuilder("松本 由美", "yumi.matsumoto@example.com", "神戸", 33)
            .nickName("ゆみちゃん")
            .gender(Student.Gender.Female)
            .remark("分析力が高い")
            .build(),

        new Student.StudentBuilder("藤田 和男", "kazuo.fujita@example.com", "仙台", 40)
            .kana("フジタ カズオ")
            .gender(Student.Gender.Male)
            .remark("リーダーシップ抜群")
            .build(),

        new Student.StudentBuilder("小林 さくら", "sakura.kobayashi@example.com", "広島", 21)
            .kana("コバヤシ サクラ")
            .nickName("さくちゃん")
            .remark("将来有望な学生")
            .build(),

        new Student.StudentBuilder("山田 太一", "taichi.yamada@example.com", "京都", 26)
            .kana("ヤマダ タイチ")
            .nickName("たいちゃん")
            .gender(Student.Gender.Male)
            .build(),

        new Student.StudentBuilder("石川 幸子", "sachiko.ishikawa@example.com", "名古屋", 37)
            .build()
    );
  }
}
