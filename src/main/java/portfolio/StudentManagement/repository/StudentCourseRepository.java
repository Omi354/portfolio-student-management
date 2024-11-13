package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
  List<StudentCourse> selectAllStudentCourseList();

  @Insert("INSERT INTO students_courses (id, student_id, course_name, start_date, end_date) VALUES(#{studentCourse.id}, #{student.id}, #{studentCourse.courseName}, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 YEAR))")
  void createStudentCourse(@Param("student") Student student,
      @Param("studentCourse") StudentCourse studentCourse);
}
