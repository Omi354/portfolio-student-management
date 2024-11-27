package portfolio.StudentManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    Student mockStudent = new Student.StudentBuilder(
        "田中太郎", "taro@test.com", "千葉県市原市", 24).build();
    String id = mockStudent.getId();

    StudentCourse mockCourse1 = new StudentCourse.StudentCourseBuilder(id,
        "Javaフルコース").build();
    StudentCourse mockCourse2 = new StudentCourse.StudentCourseBuilder(id, "AWSフルコース").build();
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
  void 受講生登録_リクエストボディから必要な情報を取得しStudentRepositoryとStudentCourseRepositoryの処理が適切に呼び出されていること() {
    // 準備
    Student mockStudent = new Student.StudentBuilder(
        "田中太郎", "taro@test.com", "千葉県市原市", 24).build();
    String id = mockStudent.getId();
    List<StudentCourse> mockStudentCourseList = List.of(
        new StudentCourse.StudentCourseBuilder(id, "Javaフルコース").build());
    StudentDetail mockstudentDetail = new StudentDetail(mockStudent, mockStudentCourseList);

    ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
    ArgumentCaptor<StudentCourse> courseCaptor = ArgumentCaptor.forClass(
        StudentCourse.class);

    // 実行
    StudentDetail actual = sut.registerStudent(mockstudentDetail);

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1))
        .createStudent(studentCaptor.capture());
    Mockito.verify(studentCourseRepository, Mockito.times(1))
        .createStudentCourse(courseCaptor.capture());

    assertThat(actual.getStudent().getFullName()).isEqualTo("田中太郎");
    assertThat(actual.getStudent().getEmail()).isEqualTo("taro@test.com");
    assertThat(actual.getStudent().getCity()).isEqualTo("千葉県市原市");
    assertThat(actual.getStudent().getAge()).isEqualTo(24);
    assertThat(actual.getStudentCourseList().getFirst().getStudentId()).isEqualTo(id);
    assertThat(actual.getStudentCourseList().getFirst().getCourseName()).isEqualTo(
        "Javaフルコース");
  }

  @Test
  void 受講生更新_適切な受講生IDがわたってくる場合かつ更新前後に差異がある場合StudentRepositoryとStudentCourseRepositoryの処理が適切に呼び出されること()
      throws StudentNotFoundException, StudentCourseNotFoundException {
    // 準備
    String id = UUID.randomUUID().toString();
    String courseId = UUID.randomUUID().toString();

    Student mockStudent = new Student.StudentBuilder(
        "田中太郎", "taro@test.com", "千葉県市原市", 24).remark("テスト")
        .useOnlyTestBuildWithId(id);

    StudentCourse mockStudentCourse = new StudentCourse
        .StudentCourseBuilder(id, "Javaフルコース").useOnlyTestBuildWithId(courseId);
    List<StudentCourse> mockStudentCourseList = List.of(mockStudentCourse);

    StudentDetail mockstudentDetail = new StudentDetail(mockStudent, mockStudentCourseList);

    Student currentStudent = new Student.StudentBuilder(
        "田中太郎", "taro@test.com", "千葉県市原市", 24).useOnlyTestBuildWithId(id);

    StudentCourse currentStudentCourse = new StudentCourse
        .StudentCourseBuilder(id, "AWSフルコース").useOnlyTestBuildWithId(courseId);
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
    Student mockStudent = new Student.StudentBuilder(
        "田中太郎", "taro@test.com", "千葉県市原市", 24).build();
    String id = mockStudent.getId();

    StudentCourse mockStudentCourse = new StudentCourse.StudentCourseBuilder(id,
        "Javaフルコース").build();
    List<StudentCourse> mockStudentCourseList = List.of(mockStudentCourse);

    StudentDetail mockstudentDetail = new StudentDetail(mockStudent, mockStudentCourseList);

    Mockito.when(studentRepository.selectStudentById(id)).thenReturn(null);

    // 実行と検証
    assertThatThrownBy(() -> sut.updateStudent(mockstudentDetail))
        .isInstanceOf(StudentNotFoundException.class)
        .hasMessageContaining("指定したIDの受講生が見つかりませんでした");

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(id);
    Mockito.verify(studentRepository, Mockito.never()).updateStudent(mockStudent);
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectCourseListByStudentId(id);
    Mockito.verify(studentCourseRepository, Mockito.never()).updateStudentCourse(mockStudentCourse);
  }

//  @Test
//  void StudentCourseの更新有無の確認_有効なIDがわたってきており変更箇所がある受講生コース情報と変更箇所がない受講生コース情報がある場合に変更箇所のみでStudentCourseRepositoryの処理が呼び出されること()
//      throws StudentCourseNotFoundException {
//    // 準備
//    String id1 = UUID.randomUUID().toString();
//    String id2 = UUID.randomUUID().toString();
//    String studentId = UUID.randomUUID().toString();
//
//    // Course1はid,studentId以外の項目に差異があり、Course2はすべての項目に差異がない設定
//    StudentCourse receivedStudentCourse1 = new StudentCourse();
//    receivedStudentCourse1.setId(id1);
//    receivedStudentCourse1.setStudentId(studentId);
//    receivedStudentCourse1.setCourseName("Javaフルコース");
//    receivedStudentCourse1.setStartDate(LocalDateTime.parse("2023-11-26T15:30:45"));
//    receivedStudentCourse1.setEndDate(LocalDateTime.parse("2024-11-26T15:30:45"));
//
//    StudentCourse receivedStudentCourse2 = new StudentCourse();
//    receivedStudentCourse2.setId(id2);
//    receivedStudentCourse2.setStudentId(studentId);
//    receivedStudentCourse2.setCourseName("AWSフルコース");
//    receivedStudentCourse2.setStartDate(LocalDateTime.parse("2022-11-26T15:30:45"));
//    receivedStudentCourse2.setEndDate(LocalDateTime.parse("2023-11-26T15:30:45"));
//
//    List<StudentCourse> receivedStudentCourseList = List.of(
//        receivedStudentCourse1, receivedStudentCourse2);
//
//    StudentCourse currentStudentCourse1 = new StudentCourse();
//    currentStudentCourse1.setId(id1);
//    currentStudentCourse1.setStudentId(studentId);
//    currentStudentCourse1.setCourseName("デザインコース");
//    currentStudentCourse1.setStartDate(LocalDateTime.parse("2024-11-26T15:30:45"));
//    currentStudentCourse1.setEndDate(LocalDateTime.parse("2025-11-26T15:30:45"));
//
//    StudentCourse currentStudentCourse2 = new StudentCourse();
//    currentStudentCourse2.setId(id2);
//    currentStudentCourse2.setStudentId(studentId);
//    currentStudentCourse2.setCourseName("AWSフルコース");
//    currentStudentCourse2.setStartDate(LocalDateTime.parse("2022-11-26T15:30:45"));
//    currentStudentCourse2.setEndDate(LocalDateTime.parse("2023-11-26T15:30:45"));
//
//    List<StudentCourse> currentStudentCourseList = List.of(
//        currentStudentCourse1, currentStudentCourse2);
//
//    // 実行
//    sut.updateStudentCourseIfModified(receivedStudentCourseList, currentStudentCourseList);
//
//    // 検証
//    Mockito.verify(studentCourseRepository, Mockito.times(1))
//        .updateStudentCourse(receivedStudentCourse1);
//    Mockito.verify(studentCourseRepository, Mockito.never())
//        .updateStudentCourse(receivedStudentCourse2);
//
//  }
//
//  @Test
//  void StudentCourseの更新有無の確認_無効なIDがわたってきた場合例外をスローすること()
//      throws StudentCourseNotFoundException {
//    // 準備
//    String id = UUID.randomUUID().toString();
//
//    StudentCourse receivedStudentCourse = new StudentCourse();
//    receivedStudentCourse.setStudentId(id);
//    receivedStudentCourse.setCourseName("Javaフルコース");
//    List<StudentCourse> receivedStudentCourseList = List.of(receivedStudentCourse);
//    List<StudentCourse> currentStudentCourseList = new ArrayList<>();
//
//    // 実行と検証
//    StudentCourseNotFoundException exception = Assertions.assertThrows(
//        StudentCourseNotFoundException.class,
//        () -> sut.updateStudentCourseIfModified(receivedStudentCourseList, currentStudentCourseList)
//    );
//
//    // 検証
//    Assertions.assertEquals("指定したIDの受講生コースが見つかりませんでした",
//        exception.getMessage());
//    Mockito.verify(studentCourseRepository, Mockito.never())
//        .updateStudentCourse(receivedStudentCourse);
//
//  }
//
//  @Test
//  void Studentの更新有無の確認_変更点がある場合適切にStudentRepositoryの処理が呼び出されること() {
//    // 準備
//    String id = UUID.randomUUID().toString();
//
//    Student receivedStudent = new Student();
//    receivedStudent.setId(id);
//    receivedStudent.setFullName("変更後");
//    receivedStudent.setKana("ヘンコウゴ");
//    receivedStudent.setNickName("変更後");
//    receivedStudent.setEmail("after@test.com");
//    receivedStudent.setCity("変更後");
//    receivedStudent.setAge(10);
//    receivedStudent.setGender(Gender.valueOf("Male"));
//    receivedStudent.setRemark("変更後");
//    receivedStudent.setIsDeleted(false);
//
//    Student currentStudent = new Student();
//    currentStudent.setId(id);
//    currentStudent.setFullName("変更前");
//    currentStudent.setKana("ヘンコウマエ");
//    currentStudent.setNickName("変更前");
//    currentStudent.setEmail("before@test.com");
//    currentStudent.setCity("変更前");
//    currentStudent.setAge(1);
//    currentStudent.setGender(Gender.valueOf("Female"));
//    currentStudent.setRemark("変更前");
//    currentStudent.setIsDeleted(true);
//
//    // 実行
//    sut.updateStudentIfModified(receivedStudent, currentStudent);
//
//    // 検証
//    Mockito.verify(studentRepository, Mockito.times(1)).updateStudent(receivedStudent);
//  }
//
//  @Test
//  void Studentの更新有無の確認_変更点がない場合StudentRepositoryの処理が呼び出されないこと() {
//    // 準備
//    String id = UUID.randomUUID().toString();
//
//    Student receivedStudent = new Student();
//    receivedStudent.setId(id);
//    receivedStudent.setFullName("変更なし");
//    receivedStudent.setKana("ヘンコウナシ");
//    receivedStudent.setNickName("変更なし");
//    receivedStudent.setEmail("not-change@test.com");
//    receivedStudent.setCity("変更なし");
//    receivedStudent.setAge(20);
//    receivedStudent.setGender(Gender.valueOf("Male"));
//    receivedStudent.setRemark("変更なし");
//    receivedStudent.setIsDeleted(false);
//
//    Student currentStudent = new Student();
//    currentStudent.setId(id);
//    currentStudent.setFullName("変更なし");
//    currentStudent.setKana("ヘンコウナシ");
//    currentStudent.setNickName("変更なし");
//    currentStudent.setEmail("not-change@test.com");
//    currentStudent.setCity("変更なし");
//    currentStudent.setAge(20);
//    currentStudent.setGender(Gender.valueOf("Male"));
//    currentStudent.setRemark("変更なし");
//    currentStudent.setIsDeleted(false);
//
//    // 実行
//    sut.updateStudentIfModified(receivedStudent, currentStudent);
//
//    // 検証
//    Mockito.verify(studentRepository, Mockito.never()).updateStudent(receivedStudent);
//  }
}