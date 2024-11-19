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
 * 受講生コース情報を扱うリポジトリ 全件検索や単一条件での検索が行えるクラスです
 */
@Mapper
public interface StudentCourseRepository {

  /**
   * 全件検索します
   *
   * @return 全件検索した受講生コースの一覧
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> selectAllCourseList();

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
