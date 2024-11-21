package portfolio.StudentManagement.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
