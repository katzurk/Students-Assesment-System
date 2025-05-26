package JustGrades.app.services;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import JustGrades.app.config.AuthHelper;
import JustGrades.app.model.Course;
import JustGrades.app.model.CourseRegistration;
import JustGrades.app.model.Grade;
import JustGrades.app.model.Student;
import JustGrades.app.model.User;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.repository.GradeRepository;
import JustGrades.app.repository.StudentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private CourseRegistrationRepository courseRegistrationRepository;
    private GradeRepository gradeRepository;
    private final AuthHelper authHelper;

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

    public Student getStudentInfo() {
        return studentRepository.findByEmail(authHelper.getCurrentUser().getEmail());
    }

    public List<Course> getAllStudentCourses() {
        return courseRegistrationRepository.findCoursesByStudentEmailAndStatus(authHelper.getCurrentUser().getEmail(), "ACTIVE");
    }

    public List<Grade> getStudentGradesByCourse(Long courseId) {
        return gradeRepository.findByStudentEmailAndCourseId(authHelper.getCurrentUser().getEmail(), courseId);
    }


    public Map<String, Double> getFinalGrades() {
        Student student = studentRepository.findByEmail(authHelper.getCurrentUser().getEmail());
        Map<String, Double> finalGrades = new HashMap<>();
        List<CourseRegistration> courses = courseRegistrationRepository.findByStudentIdAndStatus(student.getUserId(), "FINISHED");
        courses.forEach(e -> System.out.println(e.getCourse().getName()+e.getCourse().getId()));
        for(CourseRegistration course : courses) {
            Optional<Double> grade = studentRepository.getFinalGrade(student.getUserId(), course.getCourse().getId());
            grade.ifPresent(aDouble -> finalGrades.put(course.getCourse().getName(), aDouble));
        }
        return finalGrades;
    }


}
