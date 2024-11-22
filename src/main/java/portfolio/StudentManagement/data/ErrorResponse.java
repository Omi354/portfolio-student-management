package portfolio.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "エラーーレスポンスモデル")
public class ErrorResponse {

  @Schema(description = "エラーの種類", example = "error occurred")
  private String error;

  @Schema(description = "エラー詳細メッセージ", example = "エラーが発生しました")
  private String message;

  public ErrorResponse(String error, String message) {
    this.error = error;
    this.message = message;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
