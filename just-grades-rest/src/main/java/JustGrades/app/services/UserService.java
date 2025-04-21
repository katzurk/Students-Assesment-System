package JustGrades.app.services;

import JustGrades.app.model.Lecturer;
import JustGrades.app.model.Role;
import JustGrades.app.model.Student;
import JustGrades.app.model.User;
import JustGrades.app.repository.LecturerRepository;
import JustGrades.app.repository.RoleRepository;
import JustGrades.app.repository.StudentRepository;
import JustGrades.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService{

    public final static String studentRoleName = "ROLE_STUDENT";
    public final static String lecturerRoleName = "ROLE_LECTURER";
    public final static String adminRoleName = "ROLE_ADMIN";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final StudentService studentService;
    private final LecturerService lecturerService;


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByRoleName(roleName);
        user.setRole(role);

        if (role.getRoleName().equals(studentRoleName)) {
            return studentService.saveStudent(user);
        }else if (role.getRoleName().equals(lecturerRoleName)) {
            return lecturerService.saveLecturer(user);
        }
        return userRepository.save(user);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}