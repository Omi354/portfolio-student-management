package portfolio.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import portfolio.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentCourseRepositoryTest {

  @Autowired
  StudentCourseRepository sut;

  @Test
  void 受講生コース情報全件検索_受講生コース情報を全件取得できること() {
    // 準備
    String id1 = "6d96a6g0-6666-6666-6666-666666666666";
    String id2 = "7d97b7h0-7777-7777-7777-777777777777";
    String id3 = "8d98c8i0-8888-8888-8888-888888888888";
    String id4 = "9d99d9j0-9999-9999-9999-999999999999";
    String id5 = "ad9ae9k0-aaaa-aaaa-aaaa-aaaaaaaaaaaa";
    String id6 = "bd9bf9l0-bbbb-bbbb-bbbb-bbbbbbbbbbbb";
    String id7 = "cd9cg9m0-cccc-cccc-cccc-cccccccccccc";
    String id8 = "dd9dh9n0-dddd-dddd-dddd-dddddddddddd";
    String id9 = "ed9ei9o0-eeee-eeee-eeee-eeeeeeeeeeee";
    String id10 = "fd9fj9p0-ffff-ffff-ffff-ffffffffffff";
    String id11 = "gd9gi9q0-gggg-gggg-gggg-gggggggggggg";
    String id12 = "hd9hj9r0-hhhh-hhhh-hhhh-hhhhhhhhhhhh";

    // 実行
    List<StudentCourse> actual = sut.selectAllCourseList();

    // 検証
    assertThat(actual.size()).isEqualTo(12);
    assertThat(actual.get(0).getId()).isEqualTo(id1);
    assertThat(actual.get(1).getId()).isEqualTo(id2);
    assertThat(actual.get(2).getId()).isEqualTo(id3);
    assertThat(actual.get(3).getId()).isEqualTo(id4);
    assertThat(actual.get(4).getId()).isEqualTo(id5);
    assertThat(actual.get(5).getId()).isEqualTo(id6);
    assertThat(actual.get(6).getId()).isEqualTo(id7);
    assertThat(actual.get(7).getId()).isEqualTo(id8);
    assertThat(actual.get(8).getId()).isEqualTo(id9);
    assertThat(actual.get(9).getId()).isEqualTo(id10);
    assertThat(actual.get(10).getId()).isEqualTo(id11);
    assertThat(actual.get(11).getId()).isEqualTo(id12);

  }

  @ParameterizedTest
  @CsvSource({
      "0, '6d96a6g0-6666-6666-6666-666666666666', '1c91a1b0-1111-1111-1111-111111111111', 'Javaフルコース', '2024-01-01T09:00:00', '2024-06-30T17:00:00'",
      "0, '8d98c8i0-8888-8888-8888-888888888888', '2c92b2c0-2222-2222-2222-222222222222', 'デザインコース', '2024-01-01T09:00:00', '2024-06-30T17:00:00'",
      "0, 'ad9ae9k0-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '3c93c3d0-3333-3333-3333-333333333333', 'Javaフルコース', '2024-01-01T09:00:00', '2024-06-30T17:00:00'",
      "0, 'cd9cg9m0-cccc-cccc-cccc-cccccccccccc', '4c94d4e0-4444-4444-4444-444444444444', 'AWSフルコース', '2024-01-01T09:00:00', '2024-06-30T17:00:00'",
      "0, 'ed9ei9o0-eeee-eeee-eeee-eeeeeeeeeeee', '5c95e5f0-5555-5555-5555-555555555555', 'デザインコース', '2024-01-01T09:00:00', '2024-06-30T17:00:00'",
      "0, 'gd9gi9q0-gggg-gggg-gggg-gggggggggggg', '6c96f6g0-6666-6666-6666-666666666666', 'Javaフルコース', '2023-01-01T09:00:00', '2023-06-30T17:00:00'",
      "1, '7d97b7h0-7777-7777-7777-777777777777', '1c91a1b0-1111-1111-1111-111111111111', 'AWSフルコース', '2024-07-01T09:00:00', '2024-12-31T17:00:00'",
      "1, '9d99d9j0-9999-9999-9999-999999999999', '2c92b2c0-2222-2222-2222-222222222222', 'AWSフルコース', '2024-07-01T09:00:00', '2024-12-31T17:00:00'",
      "1, 'bd9bf9l0-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '3c93c3d0-3333-3333-3333-333333333333', 'デザインコース', '2024-07-01T09:00:00', '2024-12-31T17:00:00'",
      "1, 'dd9dh9n0-dddd-dddd-dddd-dddddddddddd', '4c94d4e0-4444-4444-4444-444444444444', 'Javaフルコース', '2024-07-01T09:00:00', '2024-12-31T17:00:00'",
      "1, 'fd9fj9p0-ffff-ffff-ffff-ffffffffffff', '5c95e5f0-5555-5555-5555-555555555555', 'AWSフルコース', '2024-07-01T09:00:00', '2024-12-31T17:00:00'",
      "1, 'hd9hj9r0-hhhh-hhhh-hhhh-hhhhhhhhhhhh', '6c96f6g0-6666-6666-6666-666666666666', 'AWSフルコース', '2023-07-01T09:00:00', '2023-12-31T17:00:00'"
  })
  void 受講生コース情報検索_適切な受講生IDが渡された場合_受講生IDに紐づく受講生コース情報リストが取得できること(
      int index, String id, String studentId, String courseName, LocalDateTime startDate,
      LocalDateTime endDate) {

    // 実行
    List<StudentCourse> actual = sut.selectCourseListByStudentId(studentId);

    // 検証
    assertThat(actual.get(index).getId()).isEqualTo(id);
    assertThat(actual.get(index).getCourseName()).isEqualTo(courseName);
    assertThat(actual.get(index).getStartDate()).isEqualTo(startDate);
    assertThat(actual.get(index).getEndDate()).isEqualTo(endDate);
  }

  @Test
  void 受講生コース情報検索_存在しない受講生IDが渡された場合_空のリストが返ってくること() {
    // 準備
    String id = UUID.randomUUID().toString();

    // 実行
    List<StudentCourse> actual = sut.selectCourseListByStudentId(id);

    // 検証
    assertThat(actual).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("provideNewStudentCourses")
  void 受講生コース情報登録_渡されたStudentCourseオブジェクトのレコードがDBにINSERTされること(
      StudentCourse studentCourse) {
    // 準備
    String studentId = studentCourse.getStudentId();
    int recordCountBefore = sut.selectAllCourseList().size();

    // 実行
    sut.createStudentCourse(studentCourse);
    StudentCourse actual = sut.selectCourseListByStudentId(studentId).getLast();
    int recordCountAfter = sut.selectAllCourseList().size();

    //検証
    assertThat(actual.getId()).isEqualTo(studentCourse.getId());
    assertThat(actual.getStudentId()).isEqualTo(studentCourse.getStudentId());
    assertThat(actual.getCourseName()).isEqualTo(studentCourse.getCourseName());
    assertThat(actual.getStartDate()).isEqualTo(studentCourse.getStartDate());
    assertThat(actual.getEndDate()).isEqualTo(studentCourse.getEndDate());
    assertThat(recordCountAfter).isEqualTo(recordCountBefore + 1);
  }

  @ParameterizedTest
  @CsvSource({
      "0, '6d96a6g0-6666-6666-6666-666666666666', '1c91a1b0-1111-1111-1111-111111111111', 'デザインコース', '2024-01-01T09:00:00', '2024-06-30T17:00:00'",
      "1, '7d97b7h0-7777-7777-7777-777777777777', '1c91a1b0-1111-1111-1111-111111111111', 'Javaフルコース', '2024-07-01T09:00:00', '2024-12-31T17:00:00'",
      "0, '8d98c8i0-8888-8888-8888-888888888888', '2c92b2c0-2222-2222-2222-222222222222', 'デザインコース', '2024-05-01T09:00:00', '2024-06-30T17:00:00'",
      "1, '9d99d9j0-9999-9999-9999-999999999999', '2c92b2c0-2222-2222-2222-222222222222', 'AWSフルコース', '2024-05-01T09:00:00', '2024-12-31T17:00:00'",
      "0, 'ad9ae9k0-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '3c93c3d0-3333-3333-3333-333333333333', 'Javaフルコース', '2024-01-01T09:00:00', '2025-06-30T17:00:00'",
      "1, 'bd9bf9l0-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '3c93c3d0-3333-3333-3333-333333333333', 'デザインコース', '2024-07-01T09:00:00', '2025-12-31T17:00:00'",
      "0, 'cd9cg9m0-cccc-cccc-cccc-cccccccccccc', '4c94d4e0-4444-4444-4444-444444444444', 'デザインコース', '2024-06-01T09:00:00', '2025-06-30T17:00:00'",
      "1, 'dd9dh9n0-dddd-dddd-dddd-dddddddddddd', '4c94d4e0-4444-4444-4444-444444444444', 'AWSフルコース', '2024-10-01T09:00:00', '2025-12-31T17:00:00'",
      "0, 'ed9ei9o0-eeee-eeee-eeee-eeeeeeeeeeee', '5c95e5f0-5555-5555-5555-555555555555', 'Javaフルコース', '2024-02-01T09:00:00', '2024-06-30T17:00:00'",
      "1, 'fd9fj9p0-ffff-ffff-ffff-ffffffffffff', '5c95e5f0-5555-5555-5555-555555555555', 'デザインコース', '2024-08-01T09:00:00', '2024-12-31T17:00:00'",
      "0, 'gd9gi9q0-gggg-gggg-gggg-gggggggggggg', '6c96f6g0-6666-6666-6666-666666666666', 'Javaフルコース', '2022-01-01T09:00:00', '2024-06-30T17:00:00'",
      "1, 'hd9hj9r0-hhhh-hhhh-hhhh-hhhhhhhhhhhh', '6c96f6g0-6666-6666-6666-666666666666', 'AWSフルコース', '2022-07-01T09:00:00', '2024-12-31T17:00:00'"
  })
  void 受講生コース情報更新_渡されたStudentオブジェクトのレコードでDBがUPDATEされること(
      int index, String id, String studentId, String courseName, LocalDateTime startDate,
      LocalDateTime endDate) {

    // 準備
    StudentCourse studentCourse = new StudentCourse.StudentCourseBuilder(studentId, courseName)
        .startDate(startDate).endDate(endDate).useOnlyTestBuildWithId(id);

    int recordCountBefore = sut.selectAllCourseList().size();

    // 実行
    sut.updateStudentCourse(studentCourse);
    List<StudentCourse> actual = sut.selectCourseListByStudentId(studentId);
    int recordCountAfter = sut.selectAllCourseList().size();

    // 検証
    assertThat(actual.get(index).getCourseName()).isEqualTo(studentCourse.getCourseName());
    assertThat(actual.get(index).getStartDate()).isEqualTo(studentCourse.getStartDate());
    assertThat(actual.get(index).getEndDate()).isEqualTo(studentCourse.getEndDate());
    assertThat(recordCountAfter).isEqualTo(recordCountBefore);

  }


  public static Stream<StudentCourse> provideNewStudentCourses() {
    return Stream.of(
        new StudentCourse.StudentCourseBuilder("1c91a1b0-1111-1111-1111-111111111111",
            "Javaフルコース")
            .startDate(LocalDateTime.of(2024, 1, 10, 9, 0, 0, 123456000))
            .endDate(LocalDateTime.of(2024, 6, 30, 17, 0, 0, 123456000))
            .build(),

        new StudentCourse.StudentCourseBuilder("2c92b2c0-2222-2222-2222-222222222222",
            "AWSフルコース")
            .startDate(LocalDateTime.of(2024, 7, 1, 9, 0, 0, 987654000))
            .endDate(LocalDateTime.of(2024, 12, 31, 17, 0, 0, 987654000))
            .build(),

        new StudentCourse.StudentCourseBuilder("3c93c3d0-3333-3333-3333-333333333333",
            "デザインコース")
            .startDate(LocalDateTime.of(2024, 1, 10, 9, 0, 0, 111111000))
            .endDate(LocalDateTime.of(2024, 6, 30, 17, 0, 0, 111111000))
            .build(),

        new StudentCourse.StudentCourseBuilder("4c94d4e0-4444-4444-4444-444444444444",
            "データサイエンスコース")
            .startDate(LocalDateTime.of(2024, 7, 1, 9, 0, 0, 222222000))
            .endDate(LocalDateTime.of(2025, 1, 31, 17, 0, 0, 222222000))
            .build(),

        new StudentCourse.StudentCourseBuilder("5c95e5f0-5555-5555-5555-555555555555",
            "機械学習基礎コース")
            .startDate(LocalDateTime.of(2024, 3, 1, 9, 0, 0, 333333000))
            .endDate(LocalDateTime.of(2024, 9, 30, 17, 0, 0, 333333000))
            .build()
    );
  }

}