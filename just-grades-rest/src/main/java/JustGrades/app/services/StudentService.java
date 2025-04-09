package JustGrades.app.services;

import JustGrades.app.config.SecurityConfig;
import JustGrades.app.model.Course;
import JustGrades.app.model.CourseRegistration;
import JustGrades.app.model.Student;
import JustGrades.app.model.User;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private CourseRegistrationRepository courseRegistrationRepository;

    public Student saveStudent(User user){
        Student student = new Student();
        student.setFirstName(user.getFirstName());
        student.setLastName(user.getLastName());
        student.setEmail(user.getEmail());
        student.setPassword(user.getPassword());
        student.setRole(user.getRole());

        String randomUUID = generateNumericCode(6);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        student.setStudentNumber(today.format(formatter) + randomUUID);
        student.setStatus("Active");
        student.setLibraryCardNumber("L" + randomUUID);
        return studentRepository.save(student);
    }

    public String generateNumericCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }

    public Map<String, Double> getFinalGrades() {
        System.out.println("+++++=");
        System.out.println(studentRepository.findByEmail(SecurityConfig.getCurrentUser().getEmail()));
        Student student = studentRepository.findByEmail(SecurityConfig.getCurrentUser().getEmail());
        Map<String, Double> finalGrades = new HashMap<>();
        List<CourseRegistration> courses = courseRegistrationRepository.findByStudentIdAndStatus(student.getUserId(), "Active");
        for(CourseRegistration course : courses) {
            Optional<Double> grade = studentRepository.getFinalGrade(student.getUserId(), course.getCourse().getId());
            grade.ifPresent(aDouble -> finalGrades.put(course.getCourse().getName(), aDouble));
        }
        return finalGrades;
    }


}
