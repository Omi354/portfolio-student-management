package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
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
  @Select("SELECT * FROM students")
  List<Student> selectAllStudentList();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student selectStudentById(@Param("id") String id);

  @Insert("INSERT INTO students (full_name, kana, nick_name, email, city, age, gender) VALUES(#{student.fullName},#{student.kana},#{student.nickName},#{student.email},#{student.city},#{student.age},#{student.gender})")
  @Options(useGeneratedKeys = true, keyProperty = "student.id")
  void createStudent(@Param("student") Student student);

  @Update(
      "UPDATE students SET full_name = #{student.fullName}, kana = #{student.kana}, nick_name = #{student.nickName}, email = #{student.email}, city = #{student.city}, age = #{student.age}, gender = #{student.gender}, remark = #{student.remark} "
          + "WHERE id = #{student.id}"
  )
  void updateStudent(@Param("student") Student student);
}


