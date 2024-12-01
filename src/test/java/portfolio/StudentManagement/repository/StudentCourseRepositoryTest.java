package portfolio.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
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

}