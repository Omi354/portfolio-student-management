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
import portfolio.StudentManagement.exception.StudentCourseNotFoundException;
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

    Mockito.when(studentRepository.selectStudentById(wrongId)).thenReturn(null);

    // 実行と検証
    StudentNotFoundException exception = Assertions.assertThrows(
        StudentNotFoundException.class,
        () -> sut.getStudentDetailById(wrongId)
    );

    // 検証
    Assertions.assertEquals("指定したIDの受講生が見つかりませんでした", exception.getMessage());
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(wrongId);
    Mockito.verify(studentCourseRepository, Mockito.never())
        .selectCourseListByStudentId(Mockito.anyString());
  }

  @Test
  void 受講生登録_リクエストボディから必要な情報を取得しStudentRepositoryとStudentCourseRepositoryが処理が適切に呼び出されていること() {
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
  }

  @Test
  void 受講生更新_適切な受講生IDがわたってくる場合かつ更新前後に差異がある場合StudentRepositoryとStudentCourseRepositoryが処理が適切に呼び出されること()
      throws StudentNotFoundException, StudentCourseNotFoundException {
    // 準備
    String id = UUID.randomUUID().toString();

    Student mockStudent = new Student();
    mockStudent.setId(id);
    mockStudent.setRemark("テスト");

    StudentCourse mockStudentCourse = new StudentCourse();
    mockStudentCourse.setStudentId(id);
    mockStudentCourse.setCourseName("Javaフルコース");
    List<StudentCourse> mockStudentCourseList = List.of(mockStudentCourse);

    StudentDetail mockstudentDetail = new StudentDetail(mockStudent, mockStudentCourseList);

    Student currentStudent = new Student();
    currentStudent.setId(id);
    currentStudent.setRemark("");

    StudentCourse currentStudentCourse = new StudentCourse();
    currentStudentCourse.setStudentId(id);
    currentStudentCourse.setCourseName("AWSフルコース");
    List<StudentCourse> currentStudentCourseList = List.of(currentStudentCourse);

    Mockito.when(studentRepository.selectStudentById(id)).thenReturn(currentStudent);
    Mockito.when(studentCourseRepository.selectCourseListByStudentId(id))
        .thenReturn(currentStudentCourseList);

    // 実行
    sut.updateStudent(mockstudentDetail);

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(id);
    Mockito.verify(studentRepository, Mockito.times(1)).updateStudent(mockStudent);
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectCourseListByStudentId(id);
    Mockito.verify(studentCourseRepository, Mockito.times(1))
        .updateStudentCourse(mockStudentCourse);
  }

  @Test
  void 受講生更新_存在しない受講生IDがわたってくる場合例外がスローをされること()
      throws StudentNotFoundException, StudentCourseNotFoundException {
    // 準備
    String id = UUID.randomUUID().toString();

    Student mockStudent = new Student();
    mockStudent.setId(id);

    StudentCourse mockStudentCourse = new StudentCourse();
    mockStudentCourse.setStudentId(id);
    List<StudentCourse> mockStudentCourseList = List.of(mockStudentCourse);

    StudentDetail mockstudentDetail = new StudentDetail(mockStudent, mockStudentCourseList);

    Mockito.when(studentRepository.selectStudentById(id)).thenReturn(null);

    // 実行と検証
    StudentNotFoundException exception = Assertions.assertThrows(
        StudentNotFoundException.class, () -> sut.updateStudent(mockstudentDetail)
    );

    // 検証
    Assertions.assertEquals("指定したIDの受講生が見つかりませんでした", exception.getMessage());
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(id);
    Mockito.verify(studentRepository, Mockito.never()).updateStudent(mockStudent);
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectCourseListByStudentId(id);
    Mockito.verify(studentCourseRepository, Mockito.never()).updateStudentCourse(mockStudentCourse);
  }


}