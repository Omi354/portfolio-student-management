package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import portfolio.StudentManagement.data.Student;

@Mapper
public interface StudentRepository {
  @Select("SELECT * FROM students")
  List<Student> selectAllStudentList();
}
