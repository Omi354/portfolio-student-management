package portfolio.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
    List<StudentCourse> expected = provideExistingStudentCourses().toList();

    // 実行
    List<StudentCourse> actual = sut.selectAllCourseList();

    // 検証
    assertThat(actual)
        .hasSize(12)
        .usingRecursiveFieldByFieldElementComparator()
        .isEqualTo(expected);

  }

  @ParameterizedTest
  @MethodSource("provideExistingStudentCourses")
  void 受講生コース情報検索_適切な受講生IDが渡された場合_受講生IDに紐づく受講生コース情報リストが取得できること(
      StudentCourse expected) {

    // 準備
    String studentId = expected.getStudentId();

    // 実行
    List<StudentCourse> actual = sut.selectCourseListByStudentId(studentId);

    // 検証
    assertThat(actual)
        .filteredOn(studentCourse -> studentCourse.getId().equals(expected.getId()))
        .singleElement()
        .usingRecursiveComparison()
        .isEqualTo(expected);
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
    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(studentCourse);
    assertThat(recordCountAfter).isEqualTo(recordCountBefore + 1);
  }

  @ParameterizedTest
  @MethodSource("provideUpdatedStudentCourses")
  void 受講生コース情報更新_渡されたStudentオブジェクトのレコードでDBがUPDATEされること(
      StudentCourse studentCourse) {

    // 準備
    int recordCountBefore = sut.selectAllCourseList().size();

    // 実行
    sut.updateStudentCourse(studentCourse);
    List<StudentCourse> actual = sut.selectCourseListByStudentId(studentCourse.getStudentId());
    int recordCountAfter = sut.selectAllCourseList().size();

    // 検証
    assertThat(actual)
        .filteredOn(course -> course.getId().equals(studentCourse.getId()))
        .singleElement()
        .usingRecursiveComparison()
        .isEqualTo(studentCourse);
    assertThat(recordCountAfter).isEqualTo(recordCountBefore);

  }


  private static Stream<StudentCourse> provideExistingStudentCourses() {
    return Stream.of(
        new StudentCourse.StudentCourseBuilder("1c91a1b0-1111-1111-1111-111111111111",
            "Javaフルコース")
            .startDate(LocalDateTime.parse("2024-01-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-06-30T17:00:00"))
            .useOnlyTestBuildWithId("6d96a6g0-6666-6666-6666-666666666666"),
        new StudentCourse.StudentCourseBuilder("1c91a1b0-1111-1111-1111-111111111111",
            "AWSフルコース")
            .startDate(LocalDateTime.parse("2024-07-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-12-31T17:00:00"))
            .useOnlyTestBuildWithId("7d97b7h0-7777-7777-7777-777777777777"),
        new StudentCourse.StudentCourseBuilder("2c92b2c0-2222-2222-2222-222222222222",
            "デザインコース")
            .startDate(LocalDateTime.parse("2024-01-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-06-30T17:00:00"))
            .useOnlyTestBuildWithId("8d98c8i0-8888-8888-8888-888888888888"),
        new StudentCourse.StudentCourseBuilder("2c92b2c0-2222-2222-2222-222222222222",
            "AWSフルコース")
            .startDate(LocalDateTime.parse("2024-07-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-12-31T17:00:00"))
            .useOnlyTestBuildWithId("9d99d9j0-9999-9999-9999-999999999999"),
        new StudentCourse.StudentCourseBuilder("3c93c3d0-3333-3333-3333-333333333333",
            "Javaフルコース")
            .startDate(LocalDateTime.parse("2024-01-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-06-30T17:00:00"))
            .useOnlyTestBuildWithId("ad9ae9k0-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
        new StudentCourse.StudentCourseBuilder("3c93c3d0-3333-3333-3333-333333333333",
            "デザインコース")
            .startDate(LocalDateTime.parse("2024-07-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-12-31T17:00:00"))
            .useOnlyTestBuildWithId("bd9bf9l0-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
        new StudentCourse.StudentCourseBuilder("4c94d4e0-4444-4444-4444-444444444444",
            "AWSフルコース")
            .startDate(LocalDateTime.parse("2024-01-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-06-30T17:00:00"))
            .useOnlyTestBuildWithId("cd9cg9m0-cccc-cccc-cccc-cccccccccccc"),
        new StudentCourse.StudentCourseBuilder("4c94d4e0-4444-4444-4444-444444444444",
            "Javaフルコース")
            .startDate(LocalDateTime.parse("2024-07-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-12-31T17:00:00"))
            .useOnlyTestBuildWithId("dd9dh9n0-dddd-dddd-dddd-dddddddddddd"),
        new StudentCourse.StudentCourseBuilder("5c95e5f0-5555-5555-5555-555555555555",
            "デザインコース")
            .startDate(LocalDateTime.parse("2024-01-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-06-30T17:00:00"))
            .useOnlyTestBuildWithId("ed9ei9o0-eeee-eeee-eeee-eeeeeeeeeeee"),
        new StudentCourse.StudentCourseBuilder("5c95e5f0-5555-5555-5555-555555555555",
            "AWSフルコース")
            .startDate(LocalDateTime.parse("2024-07-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-12-31T17:00:00"))
            .useOnlyTestBuildWithId("fd9fj9p0-ffff-ffff-ffff-ffffffffffff"),
        new StudentCourse.StudentCourseBuilder("6c96f6g0-6666-6666-6666-666666666666",
            "Javaフルコース")
            .startDate(LocalDateTime.parse("2023-01-01T09:00:00"))
            .endDate(LocalDateTime.parse("2023-06-30T17:00:00"))
            .useOnlyTestBuildWithId("gd9gi9q0-gggg-gggg-gggg-gggggggggggg"),
        new StudentCourse.StudentCourseBuilder("6c96f6g0-6666-6666-6666-666666666666",
            "AWSフルコース")
            .startDate(LocalDateTime.parse("2023-07-01T09:00:00"))
            .endDate(LocalDateTime.parse("2023-12-31T17:00:00"))
            .useOnlyTestBuildWithId("hd9hj9r0-hhhh-hhhh-hhhh-hhhhhhhhhhhh")
    );
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

  public static Stream<StudentCourse> provideUpdatedStudentCourses() {
    return Stream.of(
        new StudentCourse.StudentCourseBuilder("1c91a1b0-1111-1111-1111-111111111111",
            "デザインコース")
            .startDate(LocalDateTime.parse("2024-01-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-06-30T17:00:00"))
            .useOnlyTestBuildWithId("6d96a6g0-6666-6666-6666-666666666666"),

        new StudentCourse.StudentCourseBuilder("1c91a1b0-1111-1111-1111-111111111111",
            "Javaフルコース")
            .startDate(LocalDateTime.parse("2024-07-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-12-31T17:00:00"))
            .useOnlyTestBuildWithId("7d97b7h0-7777-7777-7777-777777777777"),

        new StudentCourse.StudentCourseBuilder("2c92b2c0-2222-2222-2222-222222222222",
            "デザインコース")
            .startDate(LocalDateTime.parse("2024-05-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-06-30T17:00:00"))
            .useOnlyTestBuildWithId("8d98c8i0-8888-8888-8888-888888888888"),

        new StudentCourse.StudentCourseBuilder("2c92b2c0-2222-2222-2222-222222222222",
            "AWSフルコース")
            .startDate(LocalDateTime.parse("2024-05-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-12-31T17:00:00"))
            .useOnlyTestBuildWithId("9d99d9j0-9999-9999-9999-999999999999"),

        new StudentCourse.StudentCourseBuilder("3c93c3d0-3333-3333-3333-333333333333",
            "Javaフルコース")
            .startDate(LocalDateTime.parse("2024-01-01T09:00:00"))
            .endDate(LocalDateTime.parse("2025-06-30T17:00:00"))
            .useOnlyTestBuildWithId("ad9ae9k0-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),

        new StudentCourse.StudentCourseBuilder("3c93c3d0-3333-3333-3333-333333333333",
            "デザインコース")
            .startDate(LocalDateTime.parse("2024-07-01T09:00:00"))
            .endDate(LocalDateTime.parse("2025-12-31T17:00:00"))
            .useOnlyTestBuildWithId("bd9bf9l0-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),

        new StudentCourse.StudentCourseBuilder("4c94d4e0-4444-4444-4444-444444444444",
            "デザインコース")
            .startDate(LocalDateTime.parse("2024-06-01T09:00:00"))
            .endDate(LocalDateTime.parse("2025-06-30T17:00:00"))
            .useOnlyTestBuildWithId("cd9cg9m0-cccc-cccc-cccc-cccccccccccc"),

        new StudentCourse.StudentCourseBuilder("4c94d4e0-4444-4444-4444-444444444444",
            "AWSフルコース")
            .startDate(LocalDateTime.parse("2024-10-01T09:00:00"))
            .endDate(LocalDateTime.parse("2025-12-31T17:00:00"))
            .useOnlyTestBuildWithId("dd9dh9n0-dddd-dddd-dddd-dddddddddddd"),

        new StudentCourse.StudentCourseBuilder("5c95e5f0-5555-5555-5555-555555555555",
            "Javaフルコース")
            .startDate(LocalDateTime.parse("2024-02-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-06-30T17:00:00"))
            .useOnlyTestBuildWithId("ed9ei9o0-eeee-eeee-eeee-eeeeeeeeeeee"),

        new StudentCourse.StudentCourseBuilder("5c95e5f0-5555-5555-5555-555555555555",
            "デザインコース")
            .startDate(LocalDateTime.parse("2024-08-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-12-31T17:00:00"))
            .useOnlyTestBuildWithId("fd9fj9p0-ffff-ffff-ffff-ffffffffffff"),

        new StudentCourse.StudentCourseBuilder("6c96f6g0-6666-6666-6666-666666666666",
            "Javaフルコース")
            .startDate(LocalDateTime.parse("2022-01-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-06-30T17:00:00"))
            .useOnlyTestBuildWithId("gd9gi9q0-gggg-gggg-gggg-gggggggggggg"),

        new StudentCourse.StudentCourseBuilder("6c96f6g0-6666-6666-6666-666666666666",
            "AWSフルコース")
            .startDate(LocalDateTime.parse("2022-07-01T09:00:00"))
            .endDate(LocalDateTime.parse("2024-12-31T17:00:00"))
            .useOnlyTestBuildWithId("hd9hj9r0-hhhh-hhhh-hhhh-hhhhhhhhhhhh")
    );
  }
}