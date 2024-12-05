package portfolio.StudentManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolio.StudentManagement.controller.converter.StudentConverter;
import portfolio.StudentManagement.data.EnrollmentStatus;
import portfolio.StudentManagement.data.EnrollmentStatus.Status;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.Student.Gender;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.data.StudentCourse.StudentCourseBuilder;
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.exception.EnrollmentStatusBadRequestException;
import portfolio.StudentManagement.exception.EnrollmentStatusNotFoundException;
import portfolio.StudentManagement.exception.StudentCourseNotFoundException;
import portfolio.StudentManagement.exception.StudentNotFoundException;
import portfolio.StudentManagement.repository.EnrollmentStatusRepository;
import portfolio.StudentManagement.repository.StudentCourseRepository;
import portfolio.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  StudentRepository studentRepository;

  @Mock
  StudentCourseRepository studentCourseRepository;

  @Mock
  EnrollmentStatusRepository enrollmentStatusRepository;

  @Mock
  StudentConverter converter;

  @Mock
  StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(studentRepository, studentCourseRepository, enrollmentStatusRepository,
        converter);
  }

  @Test
  void 受講生詳細の一覧検索_RepositoryとConverterの処理が適切に呼び出せていること() {
    // 事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    Mockito.when(studentRepository.selectAllStudentList()).thenReturn(studentList);
    Mockito.when(studentCourseRepository.selectAllCourseList()).thenReturn(studentCourseList);

    // 実行
    sut.getAllStudentDetailList();

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
    sut.getStudentDetailById(id);

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
    assertThatThrownBy(() -> sut.getStudentDetailById(wrongId))
        .isInstanceOf(StudentNotFoundException.class)
        .hasMessageContaining("指定したIDの受講生が見つかりませんでした");

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(wrongId);
    Mockito.verify(studentCourseRepository, Mockito.never())
        .selectCourseListByStudentId(Mockito.anyString());
  }

  @Test
  void 受講生登録_リクエストボディから必要な情報を取得しStudentRepositoryとStudentCourseRepositoryの処理が適切に呼び出されていること() {
    // 準備
    String fullName = "田中太郎";
    String kana = "タナカタロウ";
    String nickName = "たろ";
    int age = 22;
    String email = "taro@test.com";
    String city = "千葉県市原市";
    Gender gender = Gender.Male;

    Student mockStudent = new Student.StudentBuilder(fullName, email, city, age)
        .gender(gender).kana(kana).nickName(nickName).build();

    EnrollmentStatus mockStatus = EnrollmentStatus.builder().status(Status.PENDING).build();

    String studentId = mockStudent.getId();
    StudentCourse mockStudentCourse = new StudentCourseBuilder(studentId, "Javaフルコース")
        .enrollmentStatus(mockStatus).build();
    List<StudentCourse> mockStudentCourseList = List.of(mockStudentCourse);
    StudentDetail mockstudentDetail = new StudentDetail(mockStudent, mockStudentCourseList);

    ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
    ArgumentCaptor<StudentCourse> courseCaptor = ArgumentCaptor.forClass(StudentCourse.class);
    ArgumentCaptor<EnrollmentStatus> statusCaptor = ArgumentCaptor.forClass(EnrollmentStatus.class);

    // 実行
    StudentDetail actual = sut.registerStudent(mockstudentDetail);

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1))
        .createStudent(studentCaptor.capture());
    Mockito.verify(studentCourseRepository, Mockito.times(1))
        .createStudentCourse(courseCaptor.capture());
    Mockito.verify(enrollmentStatusRepository, Mockito.times(1))
        .createEnrollmentStatus(statusCaptor.capture());

    assertThat(actual.getStudent().getId()).isNotBlank();
    assertThat(actual.getStudent().getFullName()).isEqualTo(fullName);
    assertThat(actual.getStudent().getEmail()).isEqualTo(email);
    assertThat(actual.getStudent().getCity()).isEqualTo(city);
    assertThat(actual.getStudent().getAge()).isEqualTo(age);
    assertThat(actual.getStudent().getKana()).isEqualTo(kana);
    assertThat(actual.getStudent().getNickName()).isEqualTo(nickName);
    assertThat(actual.getStudent().getGender()).isEqualTo(Gender.Male);
    assertThat(actual.getStudentCourseList().getFirst().getId()).isNotBlank();
    assertThat(actual.getStudentCourseList().getFirst().getStudentId()).isEqualTo(
        actual.getStudent().getId());
    assertThat(actual.getStudentCourseList().getFirst().getCourseName()).isEqualTo(
        "Javaフルコース");
    assertThat(actual.getStudentCourseList().getFirst().getEnrollmentStatus().getStudentCourseId())
        .isNotBlank();
    assertThat(actual.getStudentCourseList().getFirst().getEnrollmentStatus().getId())
        .isNotBlank();
    assertThat(actual.getStudentCourseList().getFirst().getEnrollmentStatus().getStatus())
        .isEqualTo(Status.PENDING);
    assertThat(actual.getStudentCourseList().getFirst().getEnrollmentStatus().getCreatedAt())
        .isInstanceOf(LocalDateTime.class);

  }

  @Test
  void 受講生更新_適切な受講生IDがわたってくる場合_受講生について更新前後に差異がある場合StudentRepositoryの処理が適切に呼び出されること()
      throws StudentNotFoundException, StudentCourseNotFoundException {
    // 準備
    String id = UUID.randomUUID().toString();
    String courseId = UUID.randomUUID().toString();

    Student receivedStudent = new Student.StudentBuilder(
        "変更後", "after@test.com", "変更後", 1)
        .kana("ヘンコウゴ")
        .nickName("変更後")
        .gender(Gender.valueOf("Male"))
        .remark("変更後").useOnlyTestBuildWithId(id);

    StudentCourse receivedStudentCourse = new StudentCourse
        .StudentCourseBuilder(id, "Javaフルコース").buildWithId(courseId);
    List<StudentCourse> receivedStudentCourseList = List.of(receivedStudentCourse);

    StudentDetail receivedStudentDetail = new StudentDetail(receivedStudent,
        receivedStudentCourseList);

    Student currentStudent = new Student.StudentBuilder(
        "変更前", "before@test.com", "変更前", 100)
        .kana("ヘンコウマエ")
        .nickName("変更前")
        .gender(Gender.valueOf("Female"))
        .remark("変更前").useOnlyTestBuildWithId(id);

    StudentCourse currentStudentCourse = receivedStudentCourse;
    List<StudentCourse> currentStudentCourseList = List.of(currentStudentCourse);

    Mockito.when(studentRepository.selectStudentById(id)).thenReturn(currentStudent);
    Mockito.when(studentCourseRepository.selectCourseListByStudentId(id))
        .thenReturn(currentStudentCourseList);

    // 実行
    sut.updateStudent(receivedStudentDetail);

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(id);
    Mockito.verify(studentRepository, Mockito.times(1)).updateStudent(receivedStudent);
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectCourseListByStudentId(id);
    Mockito.verify(studentCourseRepository, Mockito.never())
        .updateStudentCourse(receivedStudentCourse);
  }

  @Test
  void 受講生更新_適切な受講生IDがわたってくる場合_受講生について更新前後に差異がない場合StudentRepositoryの処理が適切に呼び出されないこと()
      throws StudentNotFoundException, StudentCourseNotFoundException {
    // 準備
    String id = UUID.randomUUID().toString();
    String courseId = UUID.randomUUID().toString();

    Student receivedStudent = new Student.StudentBuilder(
        "変更なし", "not-change@test.com", "変更なし", 1)
        .kana("ヘンコウナシ")
        .nickName("変更なし")
        .gender(Gender.valueOf("Male"))
        .remark("変更なし").useOnlyTestBuildWithId(id);

    StudentCourse receivedStudentCourse = new StudentCourse
        .StudentCourseBuilder(id, "Javaフルコース").buildWithId(courseId);
    List<StudentCourse> receivedStudentCourseList = List.of(receivedStudentCourse);

    StudentDetail receivedStudentDetail = new StudentDetail(receivedStudent,
        receivedStudentCourseList);

    Student currentStudent = new Student.StudentBuilder(
        "変更なし", "not-change@test.com", "変更なし", 1)
        .kana("ヘンコウナシ")
        .nickName("変更なし")
        .gender(Gender.valueOf("Male"))
        .remark("変更なし").useOnlyTestBuildWithId(id);

    StudentCourse currentStudentCourse = receivedStudentCourse;
    List<StudentCourse> currentStudentCourseList = List.of(currentStudentCourse);

    Mockito.when(studentRepository.selectStudentById(id)).thenReturn(currentStudent);
    Mockito.when(studentCourseRepository.selectCourseListByStudentId(id))
        .thenReturn(currentStudentCourseList);

    // 実行
    sut.updateStudent(receivedStudentDetail);

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(id);
    Mockito.verify(studentRepository, Mockito.never()).updateStudent(receivedStudent);
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectCourseListByStudentId(id);
    Mockito.verify(studentCourseRepository, Mockito.never())
        .updateStudentCourse(receivedStudentCourse);
  }

  @Test
  void 受講生更新_適切な受講生IDがわたってくる場合_適切な受講生コースIDがわたってくる場合_複数の受講生コースインスタンスに変更がある場合_StudentCourseRepositoryの処理が適切に呼び出されること()
      throws StudentNotFoundException, StudentCourseNotFoundException {

    // 準備
    String id = UUID.randomUUID().toString();
    String courseId1 = UUID.randomUUID().toString();
    String courseId2 = UUID.randomUUID().toString();

    Student receivedStudent = new Student.StudentBuilder("田中太郎", "taro@test.com",
        "東京都西東京市", 33).useOnlyTestBuildWithId(id);
    Student currentStudent = receivedStudent;

    StudentCourse receivedStudentCourse1 = new StudentCourse.StudentCourseBuilder(id,
        "Javaフルコース").startDate(
            LocalDateTime.parse("2024-12-25T18:30:45"))
        .endDate(LocalDateTime.parse("2025-12-25T18:30:45")).buildWithId(courseId1);
    StudentCourse receivedStudentCourse2 = new StudentCourse.StudentCourseBuilder(id,
        "AWSフルコース").startDate(
            LocalDateTime.parse("2024-06-25T18:30:45"))
        .endDate(LocalDateTime.parse("2025-06-25T18:30:45")).buildWithId(courseId2);
    List<StudentCourse> receivedStudentCourseList = List.of(receivedStudentCourse1,
        receivedStudentCourse2);
    StudentDetail receivedStudentDetail = new StudentDetail(receivedStudent,
        receivedStudentCourseList);

    StudentCourse currentStudentCourse1 = new StudentCourse.StudentCourseBuilder(id,
        "マーケティングコース").startDate(
            LocalDateTime.parse("2025-12-25T18:30:45"))
        .endDate(LocalDateTime.parse("2026-12-25T18:30:45")).buildWithId(courseId1);
    StudentCourse currentStudentCourse2 = new StudentCourse.StudentCourseBuilder(id,
        "デザインコース").startDate(
            LocalDateTime.parse("2025-06-25T18:30:45"))
        .endDate(LocalDateTime.parse("2026-06-25T18:30:45")).buildWithId(courseId2);
    List<StudentCourse> currentStudentCourseList = List.of(currentStudentCourse1,
        currentStudentCourse2);

    Mockito.when(studentRepository.selectStudentById(id))
        .thenReturn(currentStudent);
    Mockito.when(studentCourseRepository.selectCourseListByStudentId(id))
        .thenReturn(currentStudentCourseList);

    // 実行
    sut.updateStudent(receivedStudentDetail);

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(id);
    Mockito.verify(studentRepository, Mockito.never()).updateStudent(receivedStudent);
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectCourseListByStudentId(id);
    Mockito.verify(studentCourseRepository, Mockito.times(1))
        .updateStudentCourse(receivedStudentCourse1);
    Mockito.verify(studentCourseRepository, Mockito.times(1))
        .updateStudentCourse(receivedStudentCourse2);
  }

  @Test
  void 受講生更新_適切な受講生IDがわたってくる場合_適切な受講生コースIDがわたってくる場合_複数の受講生コースインスタンスの内1つにだけ変更がある場合_StudentCourseRepositoryの処理が適切に呼び出されること()
      throws StudentNotFoundException, StudentCourseNotFoundException {

    // 準備
    String id = UUID.randomUUID().toString();
    String courseId1 = UUID.randomUUID().toString();
    String courseId2 = UUID.randomUUID().toString();

    Student receivedStudent = new Student.StudentBuilder("田中太郎", "taro@test.com",
        "東京都西東京市", 33).useOnlyTestBuildWithId(id);
    Student currentStudent = receivedStudent;

    StudentCourse receivedStudentCourse1 = new StudentCourse.StudentCourseBuilder(id,
        "Javaフルコース").startDate(
            LocalDateTime.parse("2024-12-25T18:30:45"))
        .endDate(LocalDateTime.parse("2025-12-25T18:30:45")).buildWithId(courseId1);
    StudentCourse receivedStudentCourse2 = new StudentCourse.StudentCourseBuilder(id,
        "AWSフルコース").startDate(
            LocalDateTime.parse("2024-06-25T18:30:45"))
        .endDate(LocalDateTime.parse("2025-06-25T18:30:45")).buildWithId(courseId2);
    List<StudentCourse> receivedStudentCourseList = List.of(receivedStudentCourse1,
        receivedStudentCourse2);
    StudentDetail receivedStudentDetail = new StudentDetail(receivedStudent,
        receivedStudentCourseList);

    StudentCourse currentStudentCourse1 = new StudentCourse.StudentCourseBuilder(id,
        "マーケティングコース").startDate(
            LocalDateTime.parse("2025-12-25T18:30:45"))
        .endDate(LocalDateTime.parse("2026-12-25T18:30:45")).buildWithId(courseId1);
    StudentCourse currentStudentCourse2 = new StudentCourse.StudentCourseBuilder(id,
        "AWSフルコース").startDate(
            LocalDateTime.parse("2024-06-25T18:30:45"))
        .endDate(LocalDateTime.parse("2025-06-25T18:30:45")).buildWithId(courseId2);
    List<StudentCourse> currentStudentCourseList = List.of(currentStudentCourse1,
        currentStudentCourse2);

    Mockito.when(studentRepository.selectStudentById(id))
        .thenReturn(currentStudent);
    Mockito.when(studentCourseRepository.selectCourseListByStudentId(id))
        .thenReturn(currentStudentCourseList);

    // 実行
    sut.updateStudent(receivedStudentDetail);

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(id);
    Mockito.verify(studentRepository, Mockito.never()).updateStudent(receivedStudent);
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectCourseListByStudentId(id);
    Mockito.verify(studentCourseRepository, Mockito.times(1))
        .updateStudentCourse(receivedStudentCourse1);
    Mockito.verify(studentCourseRepository, Mockito.never())
        .updateStudentCourse(receivedStudentCourse2);
  }

  @Test
  void 受講生更新_適切な受講生IDがわたってくる場合_適切な受講生コースIDがわたってくる場合_受講生コースに変更がない場合_StudentCourseRepositoryの処理が適切に呼び出されないこと()
      throws StudentNotFoundException, StudentCourseNotFoundException {

    // 準備
    String id = UUID.randomUUID().toString();
    String courseId1 = UUID.randomUUID().toString();
    String courseId2 = UUID.randomUUID().toString();

    Student receivedStudent = new Student.StudentBuilder("田中太郎", "taro@test.com",
        "東京都西東京市", 33).useOnlyTestBuildWithId(id);
    Student currentStudent = receivedStudent;

    StudentCourse receivedStudentCourse1 = new StudentCourse.StudentCourseBuilder(id,
        "Javaフルコース").startDate(
            LocalDateTime.parse("2024-12-25T18:30:45"))
        .endDate(LocalDateTime.parse("2025-12-25T18:30:45")).buildWithId(courseId1);
    StudentCourse receivedStudentCourse2 = new StudentCourse.StudentCourseBuilder(id,
        "AWSフルコース").startDate(
            LocalDateTime.parse("2024-06-25T18:30:45"))
        .endDate(LocalDateTime.parse("2025-06-25T18:30:45")).buildWithId(courseId2);
    List<StudentCourse> receivedStudentCourseList = List.of(receivedStudentCourse1,
        receivedStudentCourse2);
    StudentDetail receivedStudentDetail = new StudentDetail(receivedStudent,
        receivedStudentCourseList);

    StudentCourse currentStudentCourse1 = new StudentCourse.StudentCourseBuilder(id,
        "Javaフルコース").startDate(
            LocalDateTime.parse("2024-12-25T18:30:45"))
        .endDate(LocalDateTime.parse("2025-12-25T18:30:45")).buildWithId(courseId1);
    StudentCourse currentStudentCourse2 = new StudentCourse.StudentCourseBuilder(id,
        "AWSフルコース").startDate(
            LocalDateTime.parse("2024-06-25T18:30:45"))
        .endDate(LocalDateTime.parse("2025-06-25T18:30:45")).buildWithId(courseId2);
    List<StudentCourse> currentStudentCourseList = List.of(currentStudentCourse1,
        currentStudentCourse2);

    Mockito.when(studentRepository.selectStudentById(id))
        .thenReturn(currentStudent);
    Mockito.when(studentCourseRepository.selectCourseListByStudentId(id))
        .thenReturn(currentStudentCourseList);

    // 実行
    sut.updateStudent(receivedStudentDetail);

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(id);
    Mockito.verify(studentRepository, Mockito.never()).updateStudent(receivedStudent);
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectCourseListByStudentId(id);
    Mockito.verify(studentCourseRepository, Mockito.never())
        .updateStudentCourse(receivedStudentCourse1);
    Mockito.verify(studentCourseRepository, Mockito.never())
        .updateStudentCourse(receivedStudentCourse2);
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

  @Test
  void 受講生更新_存在しない受講生コース情報IDがわたってくる場合例外がスローをされること() {
    // 準備
    String id = UUID.randomUUID().toString();
    String courseId = UUID.randomUUID().toString();
    String wrongCourseId = UUID.randomUUID().toString();

    Student mockStudent = new Student.StudentBuilder(
        "田中太郎", "taro@test.com", "千葉県市原市", 24).useOnlyTestBuildWithId(id);

    StudentCourse mockStudentCourse = new StudentCourse
        .StudentCourseBuilder(id, "Javaフルコース").buildWithId(wrongCourseId);
    List<StudentCourse> mockStudentCourseList = List.of(mockStudentCourse);

    StudentDetail mockstudentDetail = new StudentDetail(mockStudent, mockStudentCourseList);

    Student currentStudent = new Student.StudentBuilder(
        "田中太郎", "taro@test.com", "千葉県市原市", 24).useOnlyTestBuildWithId(id);

    StudentCourse currentStudentCourse = new StudentCourse
        .StudentCourseBuilder(id, "AWSフルコース").buildWithId(courseId);
    List<StudentCourse> currentStudentCourseList = List.of(currentStudentCourse);

    Mockito.when(studentRepository.selectStudentById(id)).thenReturn(currentStudent);
    Mockito.when(studentCourseRepository.selectCourseListByStudentId(id))
        .thenReturn(currentStudentCourseList);

    // 実行と検証
    assertThatThrownBy(() -> sut.updateStudent(mockstudentDetail))
        .isInstanceOf(StudentCourseNotFoundException.class)
        .hasMessageContaining("指定したIDの受講生コースが見つかりませんでした");

    // 検証
    Mockito.verify(studentRepository, Mockito.times(1)).selectStudentById(id);
    Mockito.verify(studentCourseRepository, Mockito.times(1)).selectCourseListByStudentId(id);
    Mockito.verify(studentCourseRepository, Mockito.never())
        .updateStudentCourse(mockStudentCourse);
  }

  @Test
  void 申込状況更新_適切なEnrollmentStatusオブジェクトが渡された場合_適切にEnrollmentStatusが呼び出されること()
      throws EnrollmentStatusNotFoundException, EnrollmentStatusBadRequestException {
    // 準備
    String studentCourseId = UUID.randomUUID().toString();

    EnrollmentStatus enrollmentStatus = EnrollmentStatus.builder()
        .studentCourseId(studentCourseId).status(Status.IN_PROGRESS)
        .build();

    List<EnrollmentStatus> mockStatusList = List.of(EnrollmentStatus.builder()
        .id(UUID.randomUUID().toString()).studentCourseId(studentCourseId)
        .status(Status.APPROVED).createdAt(LocalDateTime.now()).build()
    );

    Mockito.when(enrollmentStatusRepository.selectAllEnrollmentStatus()
            .stream()
            .filter(v -> v.getStudentCourseId().equals(studentCourseId))
            .toList())
        .thenReturn(mockStatusList);

    // 実行
    sut.updateEnrollmentStatus(enrollmentStatus);

    // 検証
    Mockito.verify(enrollmentStatusRepository, Mockito.times(1))
        .createEnrollmentStatus(any());
  }

  @Test
  void 申込状況更新_存在しないstudentCourseIdが渡された場合_適切に例外が呼び出されること()
      throws EnrollmentStatusNotFoundException {
    // 準備
    String studentCourseId = UUID.randomUUID().toString();

    EnrollmentStatus enrollmentStatus = EnrollmentStatus.builder()
        .studentCourseId(studentCourseId).status(Status.IN_PROGRESS)
        .build();

    // 実行と検証
    assertThatThrownBy(() -> sut.updateEnrollmentStatus(enrollmentStatus))
        .isInstanceOf(EnrollmentStatusNotFoundException.class)
        .hasMessageContaining("指定した受講生コースIDのステータスは見つかりませんでした");

    // 検証
    Mockito.verify(enrollmentStatusRepository, Mockito.times(0))
        .createEnrollmentStatus(any());
  }

  @Test
  void 申込状況更新_ステータスが戻るようなオブジェクトが渡された場合_適切に例外が呼び出されること()
      throws EnrollmentStatusNotFoundException, EnrollmentStatusBadRequestException {
    // 準備
    String studentCourseId = UUID.randomUUID().toString();

    EnrollmentStatus enrollmentStatus = EnrollmentStatus.builder()
        .studentCourseId(studentCourseId).status(Status.IN_PROGRESS)
        .build();

    List<EnrollmentStatus> mockStatusList = List.of(EnrollmentStatus.builder()
        .id(UUID.randomUUID().toString()).studentCourseId(studentCourseId)
        .status(Status.COMPLETED).createdAt(LocalDateTime.now()).build()
    );

    Mockito.when(enrollmentStatusRepository.selectAllEnrollmentStatus()
            .stream()
            .filter(v -> v.getStudentCourseId().equals(studentCourseId))
            .toList())
        .thenReturn(mockStatusList);

    // 実行、検証
    assertThatThrownBy(() -> sut.updateEnrollmentStatus(enrollmentStatus))
        .isInstanceOf(EnrollmentStatusBadRequestException.class)
        .hasMessageContaining("ステータスを前に戻すことは出来ません。現在のステータス: "
            + mockStatusList.getLast().getStatus().getJapanese());

    // 検証
    Mockito.verify(enrollmentStatusRepository, Mockito.times(0))
        .createEnrollmentStatus(any());
  }
}