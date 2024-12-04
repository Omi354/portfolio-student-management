package portfolio.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
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
    List<EnrollmentStatus> expected = provideExistingStatusList().toList();

    // 実行
    List<EnrollmentStatus> actual = sut.selectAllEnrollmentStatus();

    // 検証
    assertThat(actual)
        .hasSize(12)
        .usingRecursiveFieldByFieldElementComparator()
        .isEqualTo(expected);

  }

  private Stream<EnrollmentStatus> provideExistingStatusList() {
    return Stream.of(
        EnrollmentStatus.builder()
            .id("6d96a6g0-6666-7b20-8000-000000000001")
            .studentCourseId("6d96a6g0-6666-6666-6666-666666666666")
            .status(Status.PENDING)
            .createdAt(LocalDateTime.parse("2024-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("7d97b7h0-7777-7b20-8000-000000000002")
            .studentCourseId("7d97b7h0-7777-7777-7777-777777777777")
            .status(Status.APPROVED)
            .createdAt(LocalDateTime.parse("2024-07-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("8d98c8i0-8888-7b20-8000-000000000003")
            .studentCourseId("8d98c8i0-8888-8888-8888-888888888888")
            .status(Status.IN_PROGRESS)
            .createdAt(LocalDateTime.parse("2024-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("9d99d9j0-9999-7b20-8000-000000000004")
            .studentCourseId("9d99d9j0-9999-9999-9999-999999999999")
            .status(Status.PENDING)
            .createdAt(LocalDateTime.parse("2024-07-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("ad9ae9k0-aaaa-7b20-8000-000000000005")
            .studentCourseId("ad9ae9k0-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .status(Status.APPROVED)
            .createdAt(LocalDateTime.parse("2024-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("bd9bf9l0-bbbb-7b20-8000-000000000006")
            .studentCourseId("bd9bf9l0-bbbb-bbbb-bbbb-bbbbbbbbbbbb")
            .status(Status.IN_PROGRESS)
            .createdAt(LocalDateTime.parse("2024-07-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("cd9cg9m0-cccc-7b20-8000-000000000007")
            .studentCourseId("cd9cg9m0-cccc-cccc-cccc-cccccccccccc")
            .status(Status.PENDING)
            .createdAt(LocalDateTime.parse("2024-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("dd9dh9n0-dddd-7b20-8000-000000000008")
            .studentCourseId("dd9dh9n0-dddd-dddd-dddd-dddddddddddd")
            .status(Status.IN_PROGRESS)
            .createdAt(LocalDateTime.parse("2024-07-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("ed9ei9o0-eeee-7b20-8000-000000000009")
            .studentCourseId("ed9ei9o0-eeee-eeee-eeee-eeeeeeeeeeee")
            .status(Status.APPROVED)
            .createdAt(LocalDateTime.parse("2024-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("fd9fj9p0-ffff-7b20-8000-000000000010")
            .studentCourseId("fd9fj9p0-ffff-ffff-ffff-ffffffffffff")
            .status(Status.PENDING)
            .createdAt(LocalDateTime.parse("2024-07-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("gd9gi9q0-gggg-7b20-8000-000000000011")
            .studentCourseId("gd9gi9q0-gggg-gggg-gggg-gggggggggggg")
            .status(Status.IN_PROGRESS)
            .createdAt(LocalDateTime.parse("2023-01-01T09:00:00"))
            .build(),
        EnrollmentStatus.builder()
            .id("hd9hj9r0-hhhh-7b20-8000-000000000012")
            .studentCourseId("hd9hj9r0-hhhh-hhhh-hhhh-hhhhhhhhhhhh")
            .status(Status.COMPLETED)
            .createdAt(LocalDateTime.parse("2023-07-01T09:00:00"))
            .build()
    );
  }


}