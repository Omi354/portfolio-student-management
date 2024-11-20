package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> selectAllCourseList();

  /**
   * 受講生コース情報の検索を行います。 任意の受講生のIDを指定し、任意の受講生に紐づく受講生コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> selectCourseListByStudentId(String studentId);


  /**
   * 受講生コース情報の新規登録を行います。
   *
   * @param studentCourse 受講生コース情報
   */
  @Insert("INSERT INTO students_courses (id, student_id, course_name, start_date, end_date) VALUES(#{id}, #{studentId}, #{courseName}, #{startDate}, #{endDate} )")
  void createStudentCourse(StudentCourse studentCourse);

  @Update(
      "UPDATE students_courses SET course_name = #{courseName} "
          + "WHERE id = #{id}"
  )
  void updateStudentCourse(StudentCourse studentCourse);
}
