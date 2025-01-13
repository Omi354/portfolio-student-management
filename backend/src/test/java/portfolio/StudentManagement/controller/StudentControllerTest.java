package portfolio.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import portfolio.StudentManagement.data.EnrollmentStatus;
import portfolio.StudentManagement.data.EnrollmentStatus.Status;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.Student.Gender;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.exception.EnrollmentStatusBadRequestException;
import portfolio.StudentManagement.exception.EnrollmentStatusNotFoundException;
import portfolio.StudentManagement.exception.StudentCourseNotFoundException;
import portfolio.StudentManagement.exception.StudentNotFoundException;
import portfolio.StudentManagement.service.StudentService;


@WebMvcTest(StudentController.class)
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


  @Test
  void 受講生詳細検索_クエリパラメーターが渡されなかった場合_リクエストに対して200番と空のリストが返りserviceが適切に呼び出されること()
      throws Exception {
    // 準備
    String fullName = null;
    String kana = null;
    String nickName = null;
    String email = null;
    String city = null;
    Integer minAge = null;
    Integer maxAge = null;
    Gender gender = null;
    String remark = null;

    // 実行、検証
    mockMvc.perform(get("/api/students"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    // 検証
    verify(service, times(1)).getStudentDetailList(fullName, kana, nickName, email,
        city, minAge, maxAge, gender, remark);
  }

  @Test
  void 受講生詳細検索_受講生についてのクエリパラメーターが渡された場合_リクエストに対して200番と空のリストが返りserviceが適切に呼び出されること()
      throws Exception {
    // 準備
    String fullName = "佐藤";
    String kana = "タロ";
    String nickName = "たろ";
    String email = "sato";
    String city = "東京";
    Integer minAge = 25;
    Integer maxAge = 25;
    Gender gender = Gender.Male;
    String remark = "学生";

    // 実行、検証
    mockMvc.perform(get("/api/students")
            .param("fullName", fullName)
            .param("kana", kana)
            .param("nickName", nickName)
            .param("email", email)
            .param("city", city)
            .param("minAge", minAge.toString())
            .param("maxAge", maxAge.toString())
            .param("gender", gender.toString())
            .param("remark", remark)
        )
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    // 検証
    verify(service, times(1)).getStudentDetailList(fullName, kana, nickName, email,
        city, minAge, maxAge, gender, remark);
  }


  @ParameterizedTest
  @EnumSource(Status.class)
  void 受講生詳細検索_statusについてのみリクエストパラメーターが渡された場合_serviceが呼び出され200番と空のリストが返ること(
      Status status)
      throws Exception {
    // 実行と検証
    mockMvc.perform(get("/api/students")
            .param("status", status.name()))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    // 検証
    verify(service, times(1)).getStudentDetailListByStatus(status);
  }

  @Test
  void 受講生詳細検索_受講生のクエリパラメーターと申込状況のクエリパラメーターが同時に渡された場合_400番とInvalidRequestExceptionがスローされること()
      throws Exception {
    // 準備
    String fullName = "佐藤";
    String kana = "タロ";
    String nickName = "たろ";
    String email = "sato";
    String city = "東京";
    Integer minAge = 25;
    Integer maxAge = 25;
    Gender gender = Gender.Male;
    String remark = "学生";
    Status status = Status.受講中;

    // 実行、検証
    mockMvc.perform(get("/api/students")
            .param("fullName", fullName)
            .param("kana", kana)
            .param("nickName", nickName)
            .param("email", email)
            .param("city", city)
            .param("minAge", minAge.toString())
            .param("maxAge", maxAge.toString())
            .param("gender", gender.toString())
            .param("remark", remark)
            .param("status", status.toString())
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(
            "申込状況とその他の検索条件を同時に指定することは出来ません"));

    // 検証
    verify(service, times(0)).getStudentDetailList(fullName, kana, nickName, email,
        city, minAge, maxAge, gender, remark);
    verify(service, times(0)).getStudentDetailListByStatus(status);

  }

  @ParameterizedTest
  @MethodSource("provideInvalidMaxMinAges")
  void 受講生詳細検索_maxAgeとminAgeにマイナスの値が与えられた場合_400番とInvalidRequestExceptionがスローされること(
      Integer minAge, Integer maxAge
  )
      throws Exception {
    // 準備
    String fullName = null;
    String kana = null;
    String nickName = null;
    String email = null;
    String city = null;
    Gender gender = null;
    String remark = null;

    // 実行、検証
    mockMvc.perform(get("/api/students")
            .param("minAge", Objects.nonNull(minAge) ? minAge.toString() : "")
            .param("maxAge", Objects.nonNull(maxAge) ? maxAge.toString() : "")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(
            "minAgeとmaxAgeの指定が無効です: 範囲が逆、または負の値が指定されています"));

    // 検証
    verify(service, times(0)).getStudentDetailList(fullName, kana, nickName, email,
        city, minAge, maxAge, gender, remark);

  }

  @Test
  void 受講生ID検索_存在するIDが渡された場合_受講生検索が実行され200が返ってくること()
      throws Exception {
    // 準備
    String id = UUID.randomUUID().toString();

    // 実行、検証
    mockMvc.perform(get("/api/students/{id}", id))
        .andExpect(status().isOk());

    // 検証
    verify(service, times(1)).getStudentDetailById(id);
  }

  @Test
  void 受講生ID検索_存在しないIDが渡された場合_受講生検索が実行され404とエラーメッセージが返ってくること()
      throws Exception {
    // 準備
    String id = UUID.randomUUID().toString();
    when(service.getStudentDetailById(id)).thenThrow(new StudentNotFoundException());

    // 実行、検証
    mockMvc.perform(get("/api/students/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("指定したIDの受講生が見つかりませんでした"));

    // 検証
    verify(service, times(1)).getStudentDetailById(id);
  }

  @Test
  void 受講生ID検索_誤った形式のIDが渡された場合_受講生検索が実行されず400とエラーメッセージが返ってくること()
      throws Exception {
    // 準備
    String wrongId = "aaa";

    // 実行、検証
    mockMvc.perform(get("/api/students/{id}", wrongId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(
            "入力された値が無効です。再度ご確認の上、正しい値を入力してください。"));

    // 検証
    verify(service, times(0)).getStudentDetailById(wrongId);
  }

  @Test
  void 受講生登録_適切なプロパティを持ったリクエストボディが送られた場合_受講生登録が実行され200が返ってくること()
      throws Exception {
    // 準備
    String body = """
            {
                "student": {
                    "fullName": "田中太郎",
                    "kana": "タナカタロウ",
                    "nickName": "たろ",
                    "email": "taro@test.co.jp",
                    "city": "栃木県宇都宮市",
                    "age": 45,
                    "gender": "NON_BINARY"
                },
                "studentCourseList": [
                    {
                        "courseName": "デザインコース",
                        "enrollmentStatus": {
                          "status": "仮申込"
                        }
                    }
                ]
            }
        """;
    // 実行、検証
    mockMvc.perform(post("/api/students")
            .contentType("application/json")
            .content(body))
        .andExpect(status().isOk());

    // 検証
    verify(service, times(1)).registerStudent(any());

  }

  @Test
  void 受講生登録_誤ったリクエストボディが送られた場合_400のバリデーションエラーが発生しServiceが呼び出されないこと()
      throws Exception {
    // 準備
    String body = """
            {
                "student": {
                    "fullName": "",
                    "kana": "田中太郎",
                    "nickName": "たろ",
                    "email": "taro.test.co.jp",
                    "city": "栃木県宇都宮市",
                    "age": 200,
                    "gender": "NON_BINARY"
                },
                "studentCourseList": [
                    {
                        "courseName": "",
                        "enrollmentStatus:": {
                          "status": "仮申込"
                        }
                    }
                ]
            }
        """;
    // 実行、検証
    mockMvc.perform(post("/api/students")
            .contentType("application/json")
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(
            "入力された値が無効です。再度ご確認の上、正しい値を入力してください。"));

    // 検証
    verify(service, times(0)).registerStudent(any());
  }

  @Test
  void 受講生更新_適切なプロパティを持ったリクエストボディが送られた場合_受講生更新が実行され200と成功した旨のメッセージが返ること()
      throws Exception {
    // 準備
    String body = """
            {
                "student": {
                    "id": "9b1010ec-4e5b-46e5-8c26-9b4b23159b3d",
                    "fullName": "田中太郎",
                    "kana": "タナカタロウ",
                    "nickName": "たろ",
                    "email": "taro@test.co.jp",
                    "city": "栃木県宇都宮市",
                    "age": 45,
                    "gender": "NON_BINARY",
                    "remark": "更新テスト",
                    "isDeleted": false
                },
                "studentCourseList": [
                    {
                        "id": "cfe18b23-d1b0-4872-84d3-4f09b87e1a45",
                        "studentId": "9b1010ec-4e5b-46e5-8c26-9b4b23159b3d",
                        "courseName": "デザインコース",
                        "startDate": "2024-11-29T01:28:12",
                        "endDate": "2025-11-29T01:28:12"
                    }
                ]
            }
        """;

    // 実行と検証
    mockMvc.perform(patch("/api/students/9b1010ec-4e5b-46e5-8c26-9b4b23159b3d")
            .contentType("application/json")
            .content(body))
        .andExpect(status().isOk())
        .andExpect(content().string("更新に成功しました"));

    // 検証
    verify(service, times(1)).updateStudent(any());

  }

  @Test
  void 受講生更新_誤ったプロパティを持ったリクエストボディが送られた場合_受講生更新が実行されず400とバリデーションエラーのメッセージが返ってくること()
      throws Exception {
    // 準備
    String body = """
            {
                "student": {
                    "id": "9b1010ec-4e5b-46e5-8c26-9b4b2359b3d",
                    "fullName": "",
                    "kana": "田中太郎",
                    "nickName": "たろ",
                    "email": "taro.test.co.jp",
                    "city": "",
                    "age": 200,
                    "gender": "NON_BINARY",
                    "remark": "更新テスト",
                    "isDeleted": false
                },
                "studentCourseList": [
                    {
                        "id": "cfe18b23-d1b0-4872-843-4f09b87e1a45",
                        "studentId": "9b1010ec-4e5b-6e5-8c26-9b4b23159b3d",
                        "courseName": "",
                        "startDate": "2024-11-29T01:28:12",
                        "endDate": "2025-11-29T01:28:12"
                    }
                ]
            }
        """;

    // 実行と検証
    mockMvc.perform(patch("/api/students/9b1010ec-4e5b-46e5-8c26-9b4b2359b3d")
            .contentType("application/json")
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(
            "入力された値が無効です。再度ご確認の上、正しい値を入力してください。"));

    // 検証
    verify(service, times(0)).updateStudent(any());

  }

  @Test
  void 受講生更新_studentNotFoundExceptionがスローされた場合_404とエラーメッセージが返ること()
      throws Exception {
    // 準備
    String body = """
            {
                "student": {
                    "id": "9b1010ec-4e5b-46e5-8c26-9b4b23159b3d",
                    "fullName": "田中太郎",
                    "kana": "タナカタロウ",
                    "nickName": "たろ",
                    "email": "taro@test.co.jp",
                    "city": "栃木県宇都宮市",
                    "age": 45,
                    "gender": "NON_BINARY",
                    "remark": "更新テスト",
                    "isDeleted": false
                },
                "studentCourseList": [
                    {
                        "id": "cfe18b23-d1b0-4872-84d3-4f09b87e1a45",
                        "studentId": "9b1010ec-4e5b-46e5-8c26-9b4b23159b3d",
                        "courseName": "デザインコース",
                        "startDate": "2024-11-29T01:28:12",
                        "endDate": "2025-11-29T01:28:12"
                    }
                ]
            }
        """;
    doThrow(new StudentNotFoundException())
        .when(service).updateStudent(any());

    // 実行と検証
    mockMvc.perform(patch("/api/students/9b1010ec-4e5b-46e5-8c26-9b4b23159b3d")
            .contentType("application/json")
            .content(body))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("指定したIDの受講生が見つかりませんでした"));

    // 検証
    verify(service, times(1)).updateStudent(any());

  }

  @Test
  void 受講生更新_studentCourseNotFoundExceptionがスローされた場合_404とエラーメッセージが返ること()
      throws Exception {
    // 準備
    String body = """
            {
                "student": {
                    "id": "9b1010ec-4e5b-46e5-8c26-9b4b23159b3d",
                    "fullName": "田中太郎",
                    "kana": "タナカタロウ",
                    "nickName": "たろ",
                    "email": "taro@test.co.jp",
                    "city": "栃木県宇都宮市",
                    "age": 45,
                    "gender": "NON_BINARY",
                    "remark": "更新テスト",
                    "isDeleted": false
                },
                "studentCourseList": [
                    {
                        "id": "cfe18b23-d1b0-4872-84d3-4f09b87e1a45",
                        "studentId": "9b1010ec-4e5b-46e5-8c26-9b4b23159b3d",
                        "courseName": "デザインコース",
                        "startDate": "2024-11-29T01:28:12",
                        "endDate": "2025-11-29T01:28:12"
                    }
                ]
            }
        """;
    doThrow(new StudentCourseNotFoundException())
        .when(service).updateStudent(any());

    // 実行と検証
    mockMvc.perform(patch("/api/students/9b1010ec-4e5b-46e5-8c26-9b4b23159b3d")
            .contentType("application/json")
            .content(body))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("指定したIDの受講生コースが見つかりませんでした"));

    // 検証
    verify(service, times(1)).updateStudent(any());

  }

  @Test
  void 申込状況更新_適切なRequestBodyが送られた場合_Serviceメソッドが呼び出され200と更新できたメッセージが返ること()
      throws Exception {

    // 準備
    String body = """
        {
          "studentCourseId": "6d96a6g0-6666-6666-6666-666666666666",
          "status": "受講終了"
        }
        """;

    // 実行と検証
    mockMvc.perform(post("/api/students/courses/enrollment-status")
            .contentType("application/json")
            .content(body))
        .andExpect(status().isOk())
        .andExpect(content().string("ステータスの更新に成功しました"));

    // 検証
    verify(service, times(1)).updateEnrollmentStatus(any());

  }

  @Test
  void 申込状況更新_EnrollmentStatusNotFoundExceptionがスローされた場合_404とエラーメッセージが返ること()
      throws Exception {
    // 準備
    String body = """
        {
          "studentCourseId": "6d96a6g0-6666-6666-6666-666666666666",
          "status": "受講終了"
        }
        """;

    Mockito.doThrow(new EnrollmentStatusNotFoundException())
        .when(service).updateEnrollmentStatus(any());

    // 実行と検証
    mockMvc.perform(post("/api/students/courses/enrollment-status")
            .contentType("application/json")
            .content(body))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("EnrollmentStatus Not Found"))
        .andExpect(jsonPath("$.message").value(
            "指定した受講生コースIDのステータスは見つかりませんでした"));

    // 検証
    verify(service, times(1)).updateEnrollmentStatus(any());

  }

  @Test
  void 申込状況更新_EnrollmentStatusBadRequestExceptionがスローされた場合_400とエラーメッセージが返ること()
      throws Exception {
    // 準備
    String body = """
        {
          "studentCourseId": "6d96a6g0-6666-6666-6666-666666666666",
          "status": "受講終了"
        }
        """;

    Mockito.doThrow(new EnrollmentStatusBadRequestException(
            "ステータスを前に戻すことは出来ません。現在のステータス:"))
        .when(service).updateEnrollmentStatus(any());

    // 実行と検証
    mockMvc.perform(post("/api/students/courses/enrollment-status")
            .contentType("application/json")
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("EnrollmentStatus Bad Request"))
        .andExpect(jsonPath("$.message").value(
            "ステータスを前に戻すことは出来ません。現在のステータス:"));

    // 検証
    verify(service, times(1)).updateEnrollmentStatus(any());

  }


  @ParameterizedTest
  @MethodSource("studentDataProvider")
  void 受講生詳細の受講生_入力チェックが適切に動くこと(Student student,
      String errorPlace, boolean shouldBeValid) {
    // 実行
    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    // 検証
    if (shouldBeValid) {
      assertThat(violations.size()).isEqualTo(0);
    } else {
      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(errorPlace));
    }

  }

  @ParameterizedTest
  @MethodSource("studentCourseDataProvider")
  void 受講生詳細の受講生コース情報_入力チェックが適切に動くこと(StudentCourse studentCourse,
      String errorPlace, boolean shouldBeValid) {
    // 実行
    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    // 検証
    if (shouldBeValid) {
      assertThat(violations.size()).isEqualTo(0);
    } else {
      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(errorPlace));
    }

  }

  @ParameterizedTest
  @MethodSource("enrollmentStatusDataProvider")
  void 受講生詳細の申込状況_入力チェックが適切に動くこと(EnrollmentStatus enrollmentStatus,
      String errorPlace, boolean shouldBeValid) {
    // 実行
    Set<ConstraintViolation<EnrollmentStatus>> violations = validator.validate(enrollmentStatus);

    // 検証
    if (shouldBeValid) {
      assertThat(violations.size()).isEqualTo(0);
    } else {
      assertThat(violations.size()).isEqualTo(1);
      assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(errorPlace));
    }

  }


  static Stream<Arguments> studentDataProvider() {
    return Stream.of(
        org.junit.jupiter.params.provider.Arguments.of(
            new Student.StudentBuilder("田中太郎", "taro@test.com", "千葉県成田市", 30)
                .kana("タナカタロウ").nickName("たろ").gender(Gender.valueOf("NON_BINARY"))
                .remark("入力テスト")
                .isDeleted(false).build(),
            "", true),
        org.junit.jupiter.params.provider.Arguments.of(
            new Student.StudentBuilder("", "taro@test.com", "千葉県成田市", 30)
                .kana("タナカタロウ").nickName("たろ").gender(Gender.valueOf("NON_BINARY"))
                .remark("入力テスト")
                .isDeleted(false).build(),
            "fullName", false),
        org.junit.jupiter.params.provider.Arguments.of(
            new Student.StudentBuilder("田中太郎", "taro.test.com", "千葉県成田市", 30)
                .kana("タナカタロウ").nickName("たろ").gender(Gender.valueOf("NON_BINARY"))
                .remark("入力テスト")
                .isDeleted(false).build(),
            "email", false),
        org.junit.jupiter.params.provider.Arguments.of(
            new Student.StudentBuilder("田中太郎", "", "千葉県成田市", 30)
                .kana("タナカタロウ").nickName("たろ").gender(Gender.valueOf("NON_BINARY"))
                .remark("入力テスト")
                .isDeleted(false).build(),
            "email", false),
        org.junit.jupiter.params.provider.Arguments.of(
            new Student.StudentBuilder("田中太郎", "taro@test.com", "", 30)
                .kana("タナカタロウ").nickName("たろ").gender(Gender.valueOf("NON_BINARY"))
                .remark("入力テスト")
                .isDeleted(false).build(),
            "city", false),
        org.junit.jupiter.params.provider.Arguments.of(
            new Student.StudentBuilder("田中太郎", "taro@test.com", "千葉県成田市", 2000)
                .kana("タナカタロウ").nickName("たろ").gender(Gender.valueOf("NON_BINARY"))
                .remark("入力テスト")
                .isDeleted(false).build(),
            "age", false),
        org.junit.jupiter.params.provider.Arguments.of(
            new Student.StudentBuilder("田中太郎", "taro@test.com", "千葉県成田市", 30)
                .kana("田中太郎").nickName("たろ").gender(Gender.valueOf("NON_BINARY"))
                .remark("入力テスト")
                .isDeleted(false).build(),
            "kana", false),
        org.junit.jupiter.params.provider.Arguments.of(
            new Student.StudentBuilder("田中太郎", "taro@test.com", "千葉県成田市", 30)
                .kana("たなかたろう").nickName("たろ").gender(Gender.valueOf("NON_BINARY"))
                .remark("入力テスト")
                .isDeleted(false).build(),
            "kana", false)
    );
  }

  static Stream<Arguments> studentCourseDataProvider() {
    return Stream.of(
        Arguments.of(
            new StudentCourse.StudentCourseBuilder("9b1010ec-4e5b-6e5-8c26-9b4b23159b3d",
                "Javaフルコース").startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusYears(1)).build(),
            "", true),
        Arguments.of(
            new StudentCourse.StudentCourseBuilder("9b1010ec-4e5b-6e5-8c26-9b4b23159b3d",
                "").startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusYears(1))
                .build(),
            "courseName", false)
    );
  }

  static Stream<Arguments> enrollmentStatusDataProvider() {
    return Stream.of(
        Arguments.of(
            EnrollmentStatus.builder().id(UUID.randomUUID().toString())
                .studentCourseId("6d96a6g0-6666-6666-6666-666666666666").status(Status.受講終了)
                .createdAt(LocalDateTime.now())
                .build(),
            "", true),
        Arguments.of(
            EnrollmentStatus.builder().status(Status.受講終了).build(),
            "", true),
        Arguments.of(new EnrollmentStatus(), "status", false)
    );
  }

  static Stream<Arguments> provideInvalidMaxMinAges() {
    return Stream.of(
        Arguments.of(-1, null),
        Arguments.of(null, -1),
        Arguments.of(-1, -1),
        Arguments.of(40, 30)
    );
  }


}