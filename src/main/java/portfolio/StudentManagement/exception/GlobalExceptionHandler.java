package portfolio.StudentManagement.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import portfolio.StudentManagement.data.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

  @ExceptionHandler(StudentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleStudentNotFoundException(
      StudentNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse("Student Not Found", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(StudentCourseNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleStudentCourseNotFoundException(
      StudentCourseNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse("StudentCourse Not Found", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(EnrollmentStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEnrollmentStatusNotFoundException(
      EnrollmentStatusNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse("EnrollmentStatus Not Found", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(EnrollmentStatusBadRequestException.class)
  public ResponseEntity<ErrorResponse> handleEnrollmentStatusBadRequestException(
      EnrollmentStatusBadRequestException ex) {
    ErrorResponse errorResponse = new ErrorResponse("EnrollmentStatus Bad Request",
        ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    // パラメータ名が "status" の場合のみ特別な処理をする
    if ("status".equals(ex.getName())) {
      ErrorResponse errorResponse = new ErrorResponse("EnrollmentStatus Bad Request",
          "ステータスは右記のいずれかを指定してください： '仮申込','本申込','受講中','受講終了' ");
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(errorResponse);
    }

    // それ以外は通常のエラーレスポンスを返す
    ErrorResponse errorResponse = new ErrorResponse("Bad Request",
        "Invalid parameter: " + ex.getName());
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    return getValidationErrorResponse();
  }

  @ExceptionHandler(UnexpectedTypeException.class)
  public ResponseEntity<ErrorResponse> handleUnexpectedTypeException(
      UnexpectedTypeException ex) {
    return getValidationErrorResponse();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    return getValidationErrorResponse();
  }

  private ResponseEntity<ErrorResponse> getValidationErrorResponse() {
    ErrorResponse errorResponse = new ErrorResponse("Validation Exception",
        "入力された値が無効です。再度ご確認の上、正しい値を入力してください。");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
