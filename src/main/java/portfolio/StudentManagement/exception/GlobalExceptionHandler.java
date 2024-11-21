package portfolio.StudentManagement.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

  @ExceptionHandler(StudentNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleStudentNotFoundException(
      StudentNotFoundException ex) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", "Student Not Found");
    errorResponse.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(StudentCourseNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleStudentCourseNotFoundException(
      StudentCourseNotFoundException ex) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", "Student Not Found");
    errorResponse.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(
      ConstraintViolationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("入力された値が無効です。再度ご確認の上、正しい値を入力してください。");
  }

  @ExceptionHandler(UnexpectedTypeException.class)
  public ResponseEntity<String> handleUnexpectedTypeException(
      UnexpectedTypeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("入力内容に誤りがあります。指定された形式に従って修正してください。");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("入力内容が無効です。再度ご確認の上、正しい値を入力してください。");
  }


}
