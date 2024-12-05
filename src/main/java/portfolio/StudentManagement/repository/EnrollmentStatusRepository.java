package portfolio.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import portfolio.StudentManagement.data.EnrollmentStatus;

@Mapper
public interface EnrollmentStatusRepository {

  @Select("SELECT * FROM enrollment_statuses")
  List<EnrollmentStatus> selectAllEnrollmentStatus();

  @Insert("")
  void createEnrollmentStatus(EnrollmentStatus enrollmentStatus);

}
