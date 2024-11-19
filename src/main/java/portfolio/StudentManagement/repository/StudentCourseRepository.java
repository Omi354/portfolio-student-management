package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import portfolio.StudentManagement.data.Student;
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

  
  @Insert("INSERT INTO students_courses (student_id, course_name, start_date, end_date) VALUES((SELECT id FROM students WHERE email = #{student.email}), #{studentCourse.courseName}, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 YEAR))")
  @Options(useGeneratedKeys = true, keyProperty = "studentCourse.id", keyColumn = "id")
  void createStudentCourse(@Param("student") Student student,
      @Param("studentCourse") StudentCourse studentCourse);

  @Update(
      "UPDATE students_courses SET course_name = #{courseName} "
          + "WHERE id = #{id}"
  )
  void updateStudentCourse(StudentCourse studentCourse);
}
