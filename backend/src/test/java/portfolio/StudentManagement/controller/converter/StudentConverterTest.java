package portfolio.StudentManagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.Student.Gender;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;

@ExtendWith(MockitoExtension.class)
class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生詳細リストの生成_受講生リストと受講生コースリストが適切に渡された場合_受講生とそれに紐づく受講生コースがマッピングされること() {
    // 準備
    Student student1 = new Student.StudentBuilder("田中太郎", "taro@test.com", "千葉県成田市", 30)
        .kana("タナカタロウ").nickName("たろ").gender(Gender.valueOf("NON_BINARY"))
        .remark("convertテスト").isDeleted(false).build();
    Student student2 = new Student.StudentBuilder("山田花子", "hanako@test.com", "千葉県匝瑳市", 33)
        .kana("ヤマダハナコ").nickName("ハナ").gender(Gender.valueOf("Female"))
        .remark("convertテスト").isDeleted(false).build();

    StudentCourse studentCourse1 = new StudentCourse.StudentCourseBuilder(student1.getId(),
        "Javaフルコース").startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusYears(1)).build();
    StudentCourse studentCourse2 = new StudentCourse.StudentCourseBuilder(student1.getId(),
        "AWSフルコース").startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusYears(1)).build();
    StudentCourse studentCourse3 = new StudentCourse.StudentCourseBuilder(student2.getId(),
        "デザインフルコース").startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusYears(1)).build();

    List<Student> studentList = List.of(student1, student2);
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2, studentCourse3);

    // 実行
    List<StudentDetail> actual = sut.getStudentDetailsList(studentList, studentCourseList);

    // 検証
    assertThat(actual.get(0).getStudent()).isEqualTo(student1);
    assertThat(actual.get(1).getStudent()).isEqualTo(student2);
    assertThat(actual.get(0).getStudentCourseList().get(0)).isEqualTo(studentCourse1);
    assertThat(actual.get(0).getStudentCourseList().get(1)).isEqualTo(studentCourse2);
    assertThat(actual.get(1).getStudentCourseList().get(0)).isEqualTo(studentCourse3);
  }

  @Test
  void 受講生詳細リストの生成_受講生に紐づかない受講生コースが渡された場合_空のリストが返ってくること() {
    // 準備
    Student student = new Student.StudentBuilder("田中太郎", "taro@test.com", "千葉県成田市", 30)
        .kana("タナカタロウ").nickName("たろ").gender(Gender.valueOf("NON_BINARY"))
        .remark("convertテスト").isDeleted(false).build();

    StudentCourse studentCourse = new StudentCourse.StudentCourseBuilder(
        UUID.randomUUID().toString(),
        "Javaフルコース").startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusYears(1)).build();

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    // 実行
    List<StudentDetail> actual = sut.getStudentDetailsList(studentList, studentCourseList);

    // 検証
    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEmpty();

  }
}