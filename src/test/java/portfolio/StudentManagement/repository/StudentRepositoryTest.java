package portfolio.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import portfolio.StudentManagement.data.Student;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;


  @Test
  void 受講生全件検索_削除フラグがfalseの受講生情報を全件取得できること() {
    // 準備
    List<Student> expected = provideExistingStudents().toList();

    // 実行
    List<Student> actual = sut.selectAllStudentList();

    // 検証
    assertThat(actual)
        .hasSize(5)
        .usingRecursiveFieldByFieldElementComparator()
        .isEqualTo(expected);

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
  @MethodSource("provideExistingStudents")
  void 受講生検索_適切なIDが渡された場合_IDに紐づく学生の情報を取得できること(Student expected) {
    String id = expected.getId();

    // 実行
    Student actual = sut.selectStudentById(id);

    // 検証
    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
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
    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(student);
    assertThat(recordCountAfter).isEqualTo(recordCountBefore + 1);
  }

  @ParameterizedTest
  @MethodSource("provideUpdatedStudents")
  void 受講生更新_渡されたStudentオブジェクトのレコードでDBがUPDATEされること(Student student) {

    // 準備
    String id = student.getId();
    int recordCountBefore = sut.selectAllStudentList().size();

    // 実行
    sut.updateStudent(student);
    Student actual = sut.selectStudentById(id);
    int recordCountAfter = sut.selectAllStudentList().size();

    // 検証
    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(student);
    assertThat(recordCountAfter).isEqualTo(recordCountBefore);

  }

  public static Stream<Student> provideExistingStudents() {
    return Stream.of(
        new Student.StudentBuilder("佐藤 太郎", "taro.sato@example.com", "東京", 25)
            .kana("サトウ タロウ")
            .nickName("たろちゃん")
            .gender(Student.Gender.Male)
            .remark("優秀な学生です")
            .isDeleted(false)
            .useOnlyTestBuildWithId("1c91a1b0-1111-1111-1111-111111111111"),
        new Student.StudentBuilder("鈴木 花子", "hanako.suzuki@example.com", "大阪", 28)
            .kana("スズキ ハナコ")
            .nickName("はなちゃん")
            .gender(Student.Gender.Female)
            .remark("クリエイティブ志向")
            .isDeleted(false)
            .useOnlyTestBuildWithId("2c92b2c0-2222-2222-2222-222222222222"),
        new Student.StudentBuilder("高橋 健一", "kenichi.takahashi@example.com", "名古屋", 30)
            .kana("タカハシ ケンイチ")
            .gender(Student.Gender.Male)
            .remark("リーダーシップあり")
            .isDeleted(false)
            .useOnlyTestBuildWithId("3c93c3d0-3333-3333-3333-333333333333"),
        new Student.StudentBuilder("田中 美香", "mika.tanaka@example.com", "福岡", 22)
            .kana("タナカ ミカ")
            .nickName("みかりん")
            .gender(Student.Gender.Female)
            .remark("向上心が強い")
            .isDeleted(false)
            .useOnlyTestBuildWithId("4c94d4e0-4444-4444-4444-444444444444"),
        new Student.StudentBuilder("山本 大輔", "daisuke.yamamoto@example.com", "札幌", 27)
            .kana("ヤマモト ダイスケ")
            .gender(Student.Gender.Male)
            .remark("努力家")
            .isDeleted(false)
            .useOnlyTestBuildWithId("5c95e5f0-5555-5555-5555-555555555555")
    );
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

  public static Stream<Student> provideUpdatedStudents() {
    return Stream.of(
        new Student.StudentBuilder("斎藤 太郎", "taro.saito@example.com", "埼玉県草加市", 28)
            .kana("サイトウ タロウ")
            .nickName("たろ")
            .gender(Student.Gender.NON_BINARY)
            .remark("優秀な社会人です")
            .isDeleted(false)
            .useOnlyTestBuildWithId("1c91a1b0-1111-1111-1111-111111111111"),
        new Student.StudentBuilder("鈴木 花", "hanako.suzuki@example.com", "大阪", 28)
            .kana("スズキ ハナ")
            .nickName("はなちゃん")
            .gender(Student.Gender.Female)
            .remark("クリエイティブ志向")
            .isDeleted(false)
            .useOnlyTestBuildWithId("2c92b2c0-2222-2222-2222-222222222222"),
        new Student.StudentBuilder("高橋 健一", "kenichi.takahashi@example.co.jp", "愛知県名古屋",
            30)
            .kana("タカハシ ケンイチ")
            .gender(Student.Gender.Male)
            .remark("リーダーシップあり")
            .isDeleted(false)
            .useOnlyTestBuildWithId("3c93c3d0-3333-3333-3333-333333333333"),
        new Student.StudentBuilder("田中 美香", "mika.tanaka@example.com", "福岡県福岡市", 21)
            .kana("タナカ ミカ")
            .nickName("みか")
            .gender(Student.Gender.Male)
            .remark("向上心が強い")
            .isDeleted(false)
            .useOnlyTestBuildWithId("4c94d4e0-4444-4444-4444-444444444444"),
        new Student.StudentBuilder("山本 大輔", "daisuke.yamamoto@example.com", "札幌", 27)
            .kana("ヤマモト ダイスケ")
            .gender(Student.Gender.Male)
            .remark("疲れがち")
            .isDeleted(false)
            .useOnlyTestBuildWithId("5c95e5f0-5555-5555-5555-555555555555"),
        new Student.StudentBuilder("伊藤 次郎", "jiro.ito@example.com", "仙台", 35)
            .kana("イトウ ジロウ")
            .nickName("ジロちゃん")
            .gender(Student.Gender.Male)
            .remark("退会済みの学生")
            .isDeleted(true)
            .useOnlyTestBuildWithId("6c96f6g0-6666-6666-6666-666666666666")
    );
  }

}
