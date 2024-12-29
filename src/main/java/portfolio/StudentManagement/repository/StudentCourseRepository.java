package portfolio.StudentManagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import portfolio.StudentManagement.data.EnrollmentStatus.Status;
import portfolio.StudentManagement.data.StudentCourse;

/**
 * 受講生コーステーブルを扱うリポジトリです。
 */
@Mapper
public interface StudentCourseRepository {

  /**
   * 受講生コース情報の全件検索を行います。各コースの申込状況については最新のものを取得し、受講生コース情報のフィールドに含めて返します。
   *
   * @return 受講生コース一覧（全件）
   */
  List<StudentCourse> selectAllCourseList();

  /**
   * 受講生コース情報のID検索を行います。任意の受講生のIDを指定し、任意の受講生に紐づく受講生コース情報を検索します。
   * コースの申込状況については最新のものを取得し、受講生コース情報のフィールドに含めて返します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> selectCourseListByStudentId(String studentId);

  /**
   * 受講生コース情報の条件検索を行います。コース名・受講開始日・受講修了日を指定し、該当の申込状況に合致するレコードを返します。 条件の指定がない場合には、全件を返します。
   * 申込状況については最新のものを取得し、受講生コース情報のフィールドに含めて返します。
   *
   * @param courseName 受講コース名
   * @return 検索条件に合致する受講生コース情報のリスト
   */
  List<StudentCourse> selectCourseListBySearchQuery(
      @Param("courseName") String courseName,
      @Param("startDateRangeFrom") LocalDateTime startDateRangeFrom,
      @Param("startDateRangeTo") LocalDateTime startDateRangeTo,
      @Param("endDateRangeFrom") LocalDateTime endDateRangeFrom,
      @Param("endDateRangeTo") LocalDateTime endDateRangeTo
  );

  /**
   * 受講生コース情報の申込状況検索を行います。申込状況を指定し、該当の申込状況に合致するレコードを返します。
   * 申込状況については最新のものを取得し、受講生コース情報のフィールドに含めて返します。
   *
   * @param status 申込状況
   * @return 任意の申込状況に合致する受講生コース情報のリスト
   */
  List<StudentCourse> selectCourseListWithLatestStatus(Status status);

  /**
   * 受講生コース情報の新規登録を行います。
   *
   * @param studentCourse 受講生コース情報
   */
  void createStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生コースIDに紐づくコース名の更新を行います。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);
}
