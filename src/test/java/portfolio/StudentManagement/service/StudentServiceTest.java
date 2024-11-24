package portfolio.StudentManagement.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolio.StudentManagement.controller.converter.StudentConverter;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.repository.StudentCourseRepository;
import portfolio.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  StudentRepository studentRepository;

  @Mock
  StudentCourseRepository studentCourseRepository;

  @Mock
  StudentConverter converter;

  @Mock
  StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(studentRepository, studentCourseRepository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_RepositoryとConverterの処理が適切に呼び出せていること() {
    // 事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    Mockito.when(studentRepository.selectAllStudentList()).thenReturn(studentList);
    Mockito.when(studentCourseRepository.selectAllCourseList()).thenReturn(studentCourseList);

    // 実行
    List<StudentDetail> actual = sut.getAllStudentDetailList();

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectAllStudentList();
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectAllCourseList();
    Mockito.verify(converter, Mockito.times(1))
        .getStudentDetailsList(studentList, studentCourseList);
  }

}