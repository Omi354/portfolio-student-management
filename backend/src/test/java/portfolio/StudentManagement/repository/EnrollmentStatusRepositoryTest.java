package portfolio.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import portfolio.StudentManagement.data.EnrollmentStatus;
import portfolio.StudentManagement.data.EnrollmentStatus.Status;

@MybatisTest
class EnrollmentStatusRepositoryTest {

  @Autowired
  private EnrollmentStatusRepository sut;

  @Test
  void 申込状況の全件取得ができること() {
    // 準備
    List<EnrollmentStatus> expected = provideExistingStatusList()
        .sorted(Comparator.comparing(EnrollmentStatus::getCreatedAt))
        .toList();

    // 実行
    List<EnrollmentStatus> actual = sut.selectAllEnrollmentStatus();

    // 検証
    assertThat(actual)
        .hasSize(15)
        .usingRecursiveFieldByFieldElementComparator()
        .isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("provideNewEnrollmentStatus")
  void 受講生コース情報登録_渡されたStudentCourseオブジェクトのレコードがDBにINSERTされること(
      EnrollmentStatus enrollmentStatus) {

    // 準備
    int recordCountBefore = sut.selectAllEnrollmentStatus().size();

    // 実行
    sut.createEnrollmentStatus(enrollmentStatus);
    int recordCountAfter = sut.selectAllEnrollmentStatus().size();
    EnrollmentStatus actual = sut.selectAllEnrollmentStatus().getLast();

    // 検証
    assertThat(recordCountAfter).isEqualTo(recordCountBefore + 1);
    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(enrollmentStatus);
  }


  private Stream<EnrollmentStatus> provideExistingStatusList() {
    return Stream.of(
        EnrollmentStatus.builder()
            .id("6d96a6g0-6666-7b20-8000-000000000001")
            .studentCourseId("6d96a6g0-6666-6666-6666-666666666666")
            .status(Status.仮申込)
            .createdAt(LocalDateTime.parse("2024-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("6d96a6g0-6666-7b20-8000-000000000013")
            .studentCourseId("6d96a6g0-6666-6666-6666-666666666666")
            .status(Status.本申込)
            .createdAt(LocalDateTime.parse("2024-01-11T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("6d96a6g0-6666-7b20-8000-000000000014")
            .studentCourseId("6d96a6g0-6666-6666-6666-666666666666")
            .status(Status.受講中)
            .createdAt(LocalDateTime.parse("2024-01-12T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("7d97b7h0-7777-7b20-8000-000000000002")
            .studentCourseId("7d97b7h0-7777-7777-7777-777777777777")
            .status(Status.本申込)
            .createdAt(LocalDateTime.parse("2024-07-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("8d98c8i0-8888-7b20-8000-000000000003")
            .studentCourseId("8d98c8i0-8888-8888-8888-888888888888")
            .status(Status.受講中)
            .createdAt(LocalDateTime.parse("2024-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("9d99d9j0-9999-7b20-8000-000000000004")
            .studentCourseId("9d99d9j0-9999-9999-9999-999999999999")
            .status(Status.仮申込)
            .createdAt(LocalDateTime.parse("2024-07-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("ad9ae9k0-aaaa-7b20-8000-000000000005")
            .studentCourseId("ad9ae9k0-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .status(Status.本申込)
            .createdAt(LocalDateTime.parse("2024-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("bd9bf9l0-bbbb-7b20-8000-000000000006")
            .studentCourseId("bd9bf9l0-bbbb-bbbb-bbbb-bbbbbbbbbbbb")
            .status(Status.受講中)
            .createdAt(LocalDateTime.parse("2024-07-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("bd9bf9l0-bbbb-7b20-8000-000000000015")
            .studentCourseId("bd9bf9l0-bbbb-bbbb-bbbb-bbbbbbbbbbbb")
            .status(Status.受講終了)
            .createdAt(LocalDateTime.parse("2024-12-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("cd9cg9m0-cccc-7b20-8000-000000000007")
            .studentCourseId("cd9cg9m0-cccc-cccc-cccc-cccccccccccc")
            .status(Status.仮申込)
            .createdAt(LocalDateTime.parse("2024-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("dd9dh9n0-dddd-7b20-8000-000000000008")
            .studentCourseId("dd9dh9n0-dddd-dddd-dddd-dddddddddddd")
            .status(Status.受講中)
            .createdAt(LocalDateTime.parse("2024-07-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("ed9ei9o0-eeee-7b20-8000-000000000009")
            .studentCourseId("ed9ei9o0-eeee-eeee-eeee-eeeeeeeeeeee")
            .status(Status.本申込)
            .createdAt(LocalDateTime.parse("2024-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("fd9fj9p0-ffff-7b20-8000-000000000010")
            .studentCourseId("fd9fj9p0-ffff-ffff-ffff-ffffffffffff")
            .status(Status.仮申込)
            .createdAt(LocalDateTime.parse("2024-07-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("gd9gi9q0-gggg-7b20-8000-000000000011")
            .studentCourseId("gd9gi9q0-gggg-gggg-gggg-gggggggggggg")
            .status(Status.受講中)
            .createdAt(LocalDateTime.parse("2023-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("hd9hj9r0-hhhh-7b20-8000-000000000012")
            .studentCourseId("hd9hj9r0-hhhh-hhhh-hhhh-hhhhhhhhhhhh")
            .status(Status.受講終了)
            .createdAt(LocalDateTime.parse("2023-07-01T09:00:00"))
            .build()
    );
  }


  private static Stream<EnrollmentStatus> provideNewEnrollmentStatus() {
    return Stream.of(
        EnrollmentStatus.builder()
            .id("1a1a1a1a-1111-7b20-8000-000000000013") // 新しいユニークなID
            .studentCourseId("6d96a6g0-6666-6666-6666-666666666666") // 既存のStudentCourseId
            .status(Status.受講終了) // 適切なステータス
            .createdAt(LocalDateTime.parse("2025-03-01T10:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("2b2b2b2b-2222-7b20-8000-000000000014") // 新しいユニークなID
            .studentCourseId("7d97b7h0-7777-7777-7777-777777777777") // 既存のStudentCourseId
            .status(Status.受講終了)
            .createdAt(LocalDateTime.parse("2025-06-01T11:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("3c3c3c3c-3333-7b20-8000-000000000015") // 新しいユニークなID
            .studentCourseId("8d98c8i0-8888-8888-8888-888888888888") // 既存のStudentCourseId
            .status(Status.受講終了)
            .createdAt(LocalDateTime.parse("2025-09-01T12:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("4d4d4d4d-4444-7b20-8000-000000000016") // 新しいユニークなID
            .studentCourseId("ad9ae9k0-aaaa-aaaa-aaaa-aaaaaaaaaaaa") // 既存のStudentCourseId
            .status(Status.受講終了)
            .createdAt(LocalDateTime.parse("2025-05-01T08:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("5e5e5e5e-5555-7b20-8000-000000000017") // 新しいユニークなID
            .studentCourseId("bd9bf9l0-bbbb-bbbb-bbbb-bbbbbbbbbbbb") // 既存のStudentCourseId
            .status(Status.受講終了)
            .createdAt(LocalDateTime.parse("2025-02-01T13:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("6f6f6f6f-6666-7b20-8000-000000000018") // 新しいユニークなID
            .studentCourseId("cd9cg9m0-cccc-cccc-cccc-cccccccccccc") // 既存のStudentCourseId
            .status(Status.受講終了)
            .createdAt(LocalDateTime.parse("2025-04-01T14:00:00"))
            .build()
    );
  }


}