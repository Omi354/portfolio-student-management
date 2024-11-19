package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import portfolio.StudentManagement.data.Student;

/**
 * 受講生テーブル情報を扱うリポジトリです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧（全件）
   */
  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> selectAllStudentList();

  /**
   * 受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
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


