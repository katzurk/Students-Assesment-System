package JustGrades.app.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import JustGrades.app.model.Role;
import JustGrades.app.model.User;
import JustGrades.app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentService studentService;
    private final LecturerService lecturerService;


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.valueOf(roleName));

        return switch (user.getRole()) {
            case STUDENT -> studentService.saveStudent(user);
            case LECTURER -> lecturerService.saveLecturer(user);
            case ADMIN -> userRepository.save(user);
        };
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}