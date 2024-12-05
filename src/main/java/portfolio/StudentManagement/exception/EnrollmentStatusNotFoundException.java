package portfolio.StudentManagement.exception;

public class EnrollmentStatusNotFoundException extends Exception {

  public EnrollmentStatusNotFoundException() {
    super("指定したIDのステータスは見つかりませんでした");
  }
}
