package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import portfolio.StudentManagement.data.StudentCourse;

@Mapper
public interface StudentCourseRepository {
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> selectAllStudentCourseList();
}
