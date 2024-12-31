package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.Student.Gender;

/**
 * 受講生テーブル情報を扱うリポジトリです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の検索を行います。 クエリパラメータとして受け取った値がある場合、クエリにマッチするレコードを返します。 クエリパラメータが全てnullの場合、全件を返します。
   * なお、論理削除されたレコードは対象外とします。
   *
   * @param fullName 氏名
   * @param kana     フリガナ
   * @param nickName ニックネーム
   * @param email    メールアドレス
   * @param city     地域
   * @param minAge   下限年齢
   * @param maxAge   上限年齢
   * @param gender   性別
   * @param remark   備考
   * @return 受講生一覧
   */
  List<Student> selectStudents(@Param("fullName") String fullName, @Param("kana") String kana,
      @Param("nickName") String nickName, @Param("email") String email,
      @Param("city") String city, @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge,
      @Param("gender") Gender gender, @Param("remark") String remark);

  /**
   * 受講生のID検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student selectStudentById(String id);

  /**
   * 受講生の新規登録を行います。
   *
   * @param student 受講生
   */
  void createStudent(Student student);

  /**
   * 受講生の更新を行います
   *
   * @param student 受講生
   */
  void updateStudent(Student student);
}


