package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import portfolio.StudentManagement.data.Student;

/**
 * 受講生テーブル情報を扱うリポジトリです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。論理削除されたレコードは対象外とします。
   *
   * @return 受講生一覧（全件）
   */
  List<Student> selectAllStudentList();

  /**
   * 受講生の検索を行います。
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


