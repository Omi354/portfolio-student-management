package portfolio.StudentManagement.exception;

public class StudentNotFoundException extends Exception {

  public StudentNotFoundException() {
    super("指定したIDの受講生が見つかりませんでした");
  }
}
