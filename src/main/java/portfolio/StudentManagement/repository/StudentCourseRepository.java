package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import portfolio.StudentManagement.data.StudentCourse;

/**
 * 受講生コーステーブルを扱うリポジトリです。
 */
@Mapper
public interface StudentCourseRepository {

  /**
   * 受講生コース情報の全件検索を行います。
   *
   * @return 受講生コース一覧（全件）
   */
  List<StudentCourse> selectAllCourseList();

  /**
   * 受講生コース情報の検索を行います。 任意の受講生のIDを指定し、任意の受講生に紐づく受講生コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> selectCourseListByStudentId(String studentId);

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
