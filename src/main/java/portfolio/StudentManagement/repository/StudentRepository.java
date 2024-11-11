package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import portfolio.StudentManagement.data.Student;

/**
 * 受講生情報を扱うリポジトリ
 * 全件検索や単一条件での検索が行えるクラスです
 */
@Mapper
public interface StudentRepository {
  /**
   * 全件検索します
   * @return 全件検索した受講生情報の一覧
   */
  @Select("SELECT * FROM students")
  List<Student> selectAllStudentList();
}
