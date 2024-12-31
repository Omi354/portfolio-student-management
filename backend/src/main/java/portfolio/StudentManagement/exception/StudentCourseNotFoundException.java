package portfolio.StudentManagement.exception;

public class StudentCourseNotFoundException extends Exception {

  public StudentCourseNotFoundException() {
    super("指定したIDの受講生コースが見つかりませんでした");
  }
}
