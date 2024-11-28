package portfolio.StudentManagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
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
  void 受講生詳細一覧検索_リクエストに対して200番と空のリストが返りserviceが適切に呼び出されること()
      throws Exception {
    // 実行、検証
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    // 検証
    verify(service, times(1)).getAllStudentDetailList();
  }

  @Test
  void 受講生検索_存在するIDが渡された場合_受講生検索が実行され200が返ってくること()
      throws Exception {
    // 準備
    String id = UUID.randomUUID().toString();

    // 実行、検証
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    // 検証
    verify(service, times(1)).getStudentDetailById(id);
  }

  @Test
  void 受講生検索_存在しないIDが渡された場合_受講生検索が実行され404とエラーメッセージが返ってくること()
      throws Exception {
    // 準備
    String id = UUID.randomUUID().toString();
    when(service.getStudentDetailById(id)).thenThrow(new StudentNotFoundException());

    // 実行、検証
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("指定したIDの受講生が見つかりませんでした"));

    // 検証
    verify(service, times(1)).getStudentDetailById(id);
  }

  @Test
  void 受講生検索_誤った形式のIDが渡された場合_受講生検索が実行されず400とエラーメッセージが返ってくること()
      throws Exception {
    // 準備
    String wrongId = "aaa";

    // 実行、検証
    mockMvc.perform(get("/student/{id}", wrongId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(
            "入力された値が無効です。再度ご確認の上、正しい値を入力してください。"));

    // 検証
    verify(service, times(0)).getStudentDetailById(wrongId);
  }

  @Test
  void 受講生登録_適切なプロパティを持ったリクエストボディが送られた場合_受講生登録が実行され200と送られた受講生情報が返ってくること()
      throws Exception {
    // 準備
    String body = """
            {
                "student": {
                    "fullName": "バリデーション　テスト",
                    "kana": "バリデーション　テスト",
                    "nickName": "バリデ",
                    "email": "validation-test@co.jp",
                    "city": "栃木県宇都宮市",
                    "age": 45,
                    "gender": "NON_BINARY"
                },
                "studentCourseList": [
                    {
                        "courseName": "デザインコース"
                    }
                ]
            }
        """;
    // 実行、検証
    mockMvc.perform(post("/registerStudent")
            .contentType("application/json")
            .content(body))
        .andExpect(status().isOk());

    // 検証
    verify(service, times(1)).registerStudent(any());

  }
}