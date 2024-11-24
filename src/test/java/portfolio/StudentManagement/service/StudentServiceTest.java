package portfolio.StudentManagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
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
import portfolio.StudentManagement.exception.StudentNotFoundException;
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

  @Test
  void 受講生検索_引数で渡されたIDに紐づく受講生情報が存在する場合にRepositoryの処理が適切に呼び出せていること()
      throws StudentNotFoundException {

    // 準備
    String id = UUID.randomUUID().toString();
    Student mockStudent = new Student();
    mockStudent.setId(id);

    StudentCourse mockCourse1 = new StudentCourse();
    StudentCourse mockCourse2 = new StudentCourse();
    List<StudentCourse> mockStudentCourseList = List.of(mockCourse1, mockCourse2);

    Mockito.when(studentRepository.selectStudentById(id)).thenReturn(mockStudent);
    Mockito.when(studentCourseRepository.selectCourseListByStudentId(id))
        .thenReturn(mockStudentCourseList);

    // 実行
    StudentDetail actual = sut.getStudentDetailById(id);

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(id);
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectCourseListByStudentId(id);
  }

  @Test
  void 受講生検索_引数で渡されたIDに紐づく受講生情報が存在しない場合に例外がスローされること()
      throws StudentNotFoundException {

    // 準備
    String wrongId = UUID.randomUUID().toString();

    // 実行と検証
    StudentNotFoundException exception = Assertions.assertThrows(StudentNotFoundException.class,
        () -> sut.getStudentDetailById(wrongId));

    // 検証
    Assertions.assertEquals("指定したIDの受講生が見つかりませんでした", exception.getMessage());
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(wrongId);
    Mockito.verify(studentCourseRepository, Mockito.never())
        .selectCourseListByStudentId(Mockito.anyString());
  }

  @Test
  void 受講生更新_リクエストボディから必要な情報を取得しStudentRepositoryとStudentCourseRepositoryが処理が適切に呼び出されていること() {
    // 準備
    Student mockStudent = new Student();
    List<StudentCourse> mockStudentCourseList = List.of(new StudentCourse());
    StudentDetail mockstudentDetail = new StudentDetail(mockStudent, mockStudentCourseList);
    StudentCourse mockStudentcourse = mockStudentCourseList.getFirst();

    // 実行
    StudentDetail result = sut.registerStudent(mockstudentDetail);

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1))
        .createStudent(mockStudent);
    Mockito.verify(studentCourseRepository, Mockito.times(1))
        .createStudentCourse(mockStudentcourse);
    Assertions.assertEquals(result, mockstudentDetail);
  }
}