package portfolio.StudentManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	@Autowired
	private StudentRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/allStudents")
	public String getAllStudents() {
		List<Student> allStudentsList = repository.selectAllStudents();
		String allStudents = allStudentsList.stream()
				.map(student -> student.getName() + " " + student.getAge() + "歳")
				.collect(Collectors.joining("\n"));
		return allStudents;
	}

	@GetMapping("/student")
	public String getStudent(@RequestParam String name) {
		Student student =  repository.searchByName(name);
		return student.getName() + " " + student.getAge() + "歳";
	}

	@PostMapping("/student")
	public void registerStudent(String name, int age) {
		repository.registerStudent(name, age);
	}

	@PatchMapping("/student")
	public void setStudent(String name, int age) {
		repository.updateStudent(name, age);
	}

	@DeleteMapping("student")
	public void deleteStudent(String name) {
		repository.deleteStudent(name);
	}

}
