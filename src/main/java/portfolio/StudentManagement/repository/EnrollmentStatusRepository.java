package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import portfolio.StudentManagement.data.EnrollmentStatus;

/**
 * 申込状況テーブルを扱うリポジトリです
 */
@Mapper
public interface EnrollmentStatusRepository {

  /**
   * 申込状況を単体で全件検索します
   *
   * @return 申込状況リスト（全件）
   */
  List<EnrollmentStatus> selectAllEnrollmentStatus();

  /**
   * 申込状況の新規登録を行います
   *
   * @param enrollmentStatus 申込状況
   */
  void createEnrollmentStatus(EnrollmentStatus enrollmentStatus);

}
