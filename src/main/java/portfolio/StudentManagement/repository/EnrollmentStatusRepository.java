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

  @Insert("INSERT INTO enrollment_statuses (id, student_course_id, status, created_at) VALUES (#{id}, #{studentCourseId}, #{status}, #{createdAt}) ")
  void createEnrollmentStatus(EnrollmentStatus enrollmentStatus);

}
