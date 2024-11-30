package portfolio.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import portfolio.StudentManagement.data.Student;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void test() {
    List<Student> studentList = sut.selectAllStudentList();

    assertThat(studentList.size()).isEqualTo(5);


  }
}