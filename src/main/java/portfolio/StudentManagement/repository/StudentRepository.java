package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import portfolio.StudentManagement.data.Student;

/**
 * 受講生情報を扱うリポジトリ 全件検索や単一条件での検索が行えるクラスです
 */
@Mapper
public interface StudentRepository {

  /**
   * 全件検索します
   *
   * @return 全件検索した受講生情報の一覧
   */
  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> selectAllStudentList();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student selectStudentById(String id);

  @Insert("INSERT INTO students (full_name, kana, nick_name, email, city, age, gender) VALUES(#{fullName}, #{kana}, #{nickName}, #{email}, #{city}, #{age}, #{gender})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  void createStudent(Student student);

  @Update(
      "UPDATE students SET full_name = #{fullName}, kana = #{kana}, nick_name = #{nickName}, email = #{email}, city = #{city}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = #{isDeleted} "
          + "WHERE id = #{id}"
  )
  void updateStudent(Student student);
}


