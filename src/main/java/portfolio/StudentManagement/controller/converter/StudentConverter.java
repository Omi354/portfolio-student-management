package portfolio.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import portfolio.StudentManagement.data.Student;
import portfolio.StudentManagement.data.StudentCourse;
import portfolio.StudentManagement.domain.StudentDetail;

/**
 * 受講生詳細を受講生や受講生コース情報、もしくはその逆の変換を行うコンバーターです。
 */

@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングします。 受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てます。
   *
   * @param studentList       受講生一覧（全件）
   * @param studentCourseList 受講生コース情報のリスト（全件）
   * @return 受講生詳細のリスト（全件）
   */
  public List<StudentDetail> getStudentDetailsList(List<Student> studentList,
      List<StudentCourse> studentCourseList) {
    List<StudentDetail> studentDetailsList = new ArrayList<>();
    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertedStudentCoursesList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());
      studentDetail.setStudentCourseList(convertedStudentCoursesList);
      studentDetailsList.add(studentDetail);
    });
    return studentDetailsList;
  }
}
