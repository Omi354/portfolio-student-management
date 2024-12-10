package portfolio.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.StudentManagement.controller.converter.StudentConverter;
import portfolio.StudentManagement.data.EnrollmentStatus;
import portfolio.StudentManagement.data.EnrollmentStatus.Status;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.Student.Gender;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;
import portfolio.StudentManagement.exception.EnrollmentStatusBadRequestException;
import portfolio.StudentManagement.exception.EnrollmentStatusNotFoundException;
import portfolio.StudentManagement.exception.StudentCourseNotFoundException;
import portfolio.StudentManagement.exception.StudentNotFoundException;
import portfolio.StudentManagement.repository.EnrollmentStatusRepository;
import portfolio.StudentManagement.repository.StudentCourseRepository;
import portfolio.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository studentRepository;
  private StudentCourseRepository studentCourseRepository;
  private EnrollmentStatusRepository enrollmentStatusRepository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository studentRepository,
      StudentCourseRepository studentCourseRepository,
      EnrollmentStatusRepository enrollmentStatusRepository,
      StudentConverter converter) {
    this.studentRepository = studentRepository;
    this.studentCourseRepository = studentCourseRepository;
    this.enrollmentStatusRepository = enrollmentStatusRepository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の検索を行います。
   * クエリパラメータとして受け取った値がある場合、クエリにマッチする受講生とそれに紐づく受講生コースリストを含んだ受講生リストを返します。クエリパラメータが全てnullの場合、全件を返します。
   * なお、論理削除されたレコードは対象外とします。
   *
   * @return 受講生詳細リスト
   */
  public List<StudentDetail> getStudentDetailList(String fullName, String kana,
      String nickName, String email,
      String city, Integer minAge, Integer maxAge,
      Gender gender, String remark) {
    List<Student> studentList = studentRepository.selectStudents(fullName, kana, nickName, email,
        city, minAge, maxAge, gender, remark);
    List<StudentCourse> studentCourseList = studentCourseRepository.selectAllCourseList();

    return converter.getStudentDetailsList(studentList, studentCourseList);
  }

  /**
   * 受講生検索です。 IDに紐づく任意の受講生の情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細（受講生と受講コース情報）
   */
  public StudentDetail getStudentDetailById(String id) throws StudentNotFoundException {
    Student student = studentRepository.selectStudentById(id);
    // 指定したID該当する受講生が存在しない場合、エラーを発生させます
    if (student == null) {
      throw new StudentNotFoundException();
    }

    List<StudentCourse> studentCourseList = studentCourseRepository.selectCourseListByStudentId(id);
    return new StudentDetail(student, studentCourseList);
  }

  /**
   * 申込状況を指定して受講生詳細を検索します。 引数に受け取った申込状況に合致する受講生コース情報リストを取得し、受講生コース情報に紐づく受講生を取得します。
   *
   * @param status 申込状況のステータス
   * @return 受講生詳細
   */
  public List<StudentDetail> getStudentDetailListByStatus(Status status) {
    List<StudentCourse> studentCourseList = studentCourseRepository
        .selectCourseListWithLatestStatus(status);
    List<Student> studentList = studentCourseList.stream()
        .map(studentCourse -> studentRepository.selectStudentById(studentCourse.getStudentId()))
        .toList();
    return converter.getStudentDetailsList(studentList, studentCourseList);
  }

  /**
   * 受講生情報と受講生コース情報と申込状況をそれぞれ登録します。 登録前に受講生情報については、デフォルト値としてUUIDをランダム, 備考を空欄、キャンセルフラグをfalseに設定します。
   * 登録前に受講生コース情報については、デフォルト値としてUUIDをランダム、受講生IDを同時に作成される受講生情報のID、受講開始日をレコード登録日時、受講修了予定日を受講開始日から１年後に設定します。
   * 登録前に申込状況については、デフォルト値としてUUIDをランダム、受講生コースIDを同時に作成される受講生コース情報のID、作成日を登録日時に設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 新規登録された受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student receivedStudent = studentDetail.getStudent();
    StudentCourse receivedStudentCourse = studentDetail.getStudentCourseList().getFirst();
    EnrollmentStatus receivedEnrollmentStatus = receivedStudentCourse.getEnrollmentStatus();

    String fullName = receivedStudent.getFullName();
    String kana = receivedStudent.getKana();
    String nickName = receivedStudent.getNickName();
    String email = receivedStudent.getEmail();
    String city = receivedStudent.getCity();
    int age = receivedStudent.getAge();
    Gender gender = receivedStudent.getGender();
    Student newStudent = new Student.StudentBuilder(fullName, email, city, age)
        .kana(kana).nickName(nickName).gender(gender).build();

    String courseId = UUID.randomUUID().toString();
    Status status = receivedEnrollmentStatus.getStatus();

    EnrollmentStatus newEnrollmentStatus = EnrollmentStatus.builder()
        .id(UUID.randomUUID().toString())
        .studentCourseId(courseId)
        .createdAt(LocalDateTime.now()).status(status)
        .build();

    String studentId = newStudent.getId();
    String courseName = receivedStudentCourse.getCourseName();
    StudentCourse newStudentCourse = new StudentCourse
        .StudentCourseBuilder(studentId, courseName).enrollmentStatus(newEnrollmentStatus)
        .buildWithId(courseId);

    studentRepository.createStudent(newStudent);
    studentCourseRepository.createStudentCourse(newStudentCourse);
    enrollmentStatusRepository.createEnrollmentStatus(newEnrollmentStatus);
    return new StudentDetail(newStudent, List.of(newStudentCourse));
  }

  /**
   * 受講生情報と受講生コース情報をそれぞれ更新します。
   * 更新処理前に受け取った受講生情報・受講生コース情報と、登録されている受講生情報・受講生コース情報を比較し、修正点が有る場合のみ処理を実行します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail)
      throws StudentNotFoundException, StudentCourseNotFoundException {
    // リクエストとして受け取った受講生と受講生コース情報を定義します
    Student receivedStudent = studentDetail.getStudent();
    List<StudentCourse> receivedStudentCourseList = studentDetail.getStudentCourseList();

    // リクエストとして受け取った受講生IDから、現時点でDBに登録されている受講生と受講生コース情報を定義します
    String studentId = receivedStudent.getId();
    Student currentStudent = studentRepository.selectStudentById(studentId);
    List<StudentCourse> currentStudentCourseList = studentCourseRepository
        .selectCourseListByStudentId(studentId);

    // リクエストとして受け取った受講生IDに該当する受講生が存在しない場合にエラーを発生させます
    if (currentStudent == null) {
      throw new StudentNotFoundException();
    }

    // リクエストとして受け取った受講生情報・受講生コース情報とDBに登録されている受講生・受講生コース情報に差異がある場合に更新処理を実行します
    updateStudentIfModified(receivedStudent, currentStudent);
    updateStudentCourseIfModified(receivedStudentCourseList, currentStudentCourseList);
  }


  /**
   * 申込状況を更新します。後ろに戻るようなステータス更新や適切に受講生コース情報に紐づいていない場合にはエラーを投げます。
   * 分析に使用できるよう、受け取った申込状況オブジェクトを元に新しい申込状況オブジェクトを生成して新規登録します。
   *
   * @param receivedEnrollmentStatus 更新希望の申込状況オブジェクト
   * @throws EnrollmentStatusNotFoundException   　更新対象の申込状況に紐づく受講生コース情報がない場合に投げられるエラー
   * @throws EnrollmentStatusBadRequestException 　申込状況が後ろに戻るような場合に投げられるエラー
   */
  public void updateEnrollmentStatus(EnrollmentStatus receivedEnrollmentStatus)
      throws EnrollmentStatusNotFoundException, EnrollmentStatusBadRequestException {

    String receivedStudentCourseId = receivedEnrollmentStatus.getStudentCourseId();
    Status receivedStatus = receivedEnrollmentStatus.getStatus();

    verifyEnrollmentStatus(receivedEnrollmentStatus);

    EnrollmentStatus newEnrollmentStatus = EnrollmentStatus.builder()
        .id(UUID.randomUUID().toString()).studentCourseId(receivedStudentCourseId)
        .status(receivedStatus)
        .createdAt(LocalDateTime.now())
        .build();

    enrollmentStatusRepository.createEnrollmentStatus(newEnrollmentStatus);
  }

  /**
   * 更新希望の申込状況が更新可能か検証します。 与えられた申込状況に紐づく受講生コース情報がない場合にはエラーを投げます。
   * また、申込状況は不可逆であるため、後ろに戻るような変更が渡された場合エラーを投げます。 検証に通った場合には、何も返さず、このメソッドの後続の処理が実行できます。
   *
   * @param receivedEnrollmentStatus 更新対象の申込状況オブジェクト
   * @throws EnrollmentStatusNotFoundException   更新対象の申込状況に紐づく受講生コース情報がない場合に投げられるエラー
   * @throws EnrollmentStatusBadRequestException 申込状況が後ろに戻るような場合に投げられるエラー
   */
  private void verifyEnrollmentStatus(EnrollmentStatus receivedEnrollmentStatus)
      throws EnrollmentStatusNotFoundException, EnrollmentStatusBadRequestException {

    // 引数のEnrollmentStatusオブジェクトから受講生コース情報IDとステータスを取得
    String receivedStudentCourseId = receivedEnrollmentStatus.getStudentCourseId();
    Status receivedStatus = receivedEnrollmentStatus.getStatus();

    // 全申込状況から受講生コース情報IDにマッチするものをリスト化
    List<EnrollmentStatus> matchingEnrollmentStatuses = enrollmentStatusRepository.selectAllEnrollmentStatus()
        .stream()
        .filter(enrollmentStatus -> enrollmentStatus.getStudentCourseId()
            .equals(receivedStudentCourseId))
        .toList();

    // リストが空だった場合＝受講生コース情報IDに紐づく申込状況がない場合にエラーを投げる
    if (matchingEnrollmentStatuses.isEmpty()) {
      throw new EnrollmentStatusNotFoundException();
    }

    // ステータスの不可逆チェックのためのマップを作成
    Status currentStatus = matchingEnrollmentStatuses.getLast().getStatus();
    Map<Status, Integer> mapForCompareStatus = Map.of(
        Status.仮申込, 1,
        Status.本申込, 2,
        Status.受講中, 3,
        Status.受講終了, 4
    );

    // マップを元にステータスに応じた数値を定義
    Integer receivedStatusNumber = mapForCompareStatus.get(receivedStatus);
    Integer currentStatusNumber = mapForCompareStatus.get(currentStatus);

    // 現在のステータスのほうが更新希望のステータスよりも進んでいる、あるいは同じ場合、エラーを投げる
    if (currentStatusNumber >= receivedStatusNumber) {
      throw new EnrollmentStatusBadRequestException(
          "ステータスを前に戻すことは出来ません。現在のステータス: " + currentStatus);
    }
  }

  /**
   * リクエストとして受け取った受講生コース情報と現時点でDBに登録されている受講生コース情報に差異がある場合に更新処理を実行します。
   * 受講生コース情報はリスト渡されるため、ループを回して中身の受講生コース情報を取り出して処理します。
   *
   * @param receivedStudentCourseList リクエストとして受け取った受講生コース
   * @param currentStudentCourseList  現時点でDBに登録されている受講生コース情報
   */
  private void updateStudentCourseIfModified(List<StudentCourse> receivedStudentCourseList,
      List<StudentCourse> currentStudentCourseList) throws StudentCourseNotFoundException {

    // currentStudentCourseList　を　{"id", {studentCourse}} のマップに変換します
    Map<String, StudentCourse> currentStudentCourseMap = currentStudentCourseList.stream()
        .collect(
            Collectors.toMap(StudentCourse::getId, studentCourse -> studentCourse, (a, b) -> b));
    // receivedStudentCourseListをfor文で回し、中身のreceivedStudentCourseを一つずつ取り出します
    for (StudentCourse receivedStudentCourse : receivedStudentCourseList) {
      // receivedStudentCourseのIDに紐づく、現時点で登録されている受講生コース情報を取得します
      StudentCourse currentStudentCourse = currentStudentCourseMap
          .get(receivedStudentCourse.getId());
      // receivedStudentCourseのIDに紐づく受講生コース情報が存在しない場合、エラーを発生させます
      if (currentStudentCourse == null) {
        throw new StudentCourseNotFoundException();
      }
      // 現時点で登録されているコース名と、受け取ったコース名が異なる場合にRepositoryにidと変更後のコース名を渡します
      if (!receivedStudentCourse.equals(currentStudentCourse)) {
        studentCourseRepository.updateStudentCourse(receivedStudentCourse);
      }
    }
  }

  /**
   * リクエストとして受け取った受講生情報と現時点でDBに登録されている受講生情報に差異がある場合に更新処理を実行します。
   *
   * @param receivedStudent リクエストとして受け取った受講生情報
   * @param currentStudent  現時点でDBに登録されている受講生情報
   */
  private void updateStudentIfModified(Student receivedStudent, Student currentStudent) {
    if (!receivedStudent.equals(currentStudent)) {
      studentRepository.updateStudent(receivedStudent);
    }
  }
}
