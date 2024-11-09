package portfolio.StudentManagement;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	public StudentManagementApplication() {
		studentInfoMap.put("name", "omi");
		studentInfoMap.put("age", "92");
	}

	private Map<String, String> studentInfoMap = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/studentInfo")
	public Map<String, String> getStudentInfo() {
		return studentInfoMap;
	}

	@GetMapping("/studentName")
	public String getStudentName() {
		return studentInfoMap.get("name");
	}

	@GetMapping("/studentAge")
	public String getStudentAge() {
		return studentInfoMap.get("age");
	}

	@PostMapping("/studentInfo")
	public void setStudentInfoMap(String name, String age) {
		studentInfoMap.put("name", name);
		studentInfoMap.put("age", age);
	}

	@PostMapping("/studentName")
	public void setStudentName(String name) {
		studentInfoMap.put("name", name);
	}

	@PostMapping("/studentAge")
	public void setStudentAge(String age) {
		studentInfoMap.put("age", age);
	}

}
