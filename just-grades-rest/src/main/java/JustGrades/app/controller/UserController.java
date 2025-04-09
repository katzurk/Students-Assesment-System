package JustGrades.app.controller;

import JustGrades.app.config.SecurityConfig;
import JustGrades.app.model.Student;
import JustGrades.app.model.User;
import JustGrades.app.services.StudentService;
import JustGrades.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final StudentService studentService;
    private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, StudentService studentService, DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.studentService = studentService;
        this.dataSourceTransactionManagerAutoConfiguration = dataSourceTransactionManagerAutoConfiguration;
    }



    @PostMapping("/auth/login")
    public String login(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            if(existingUser.getRole().getRoleName().equals(UserService.studentRoleName)){
                System.out.println(SecurityConfig.getCurrentUser());
                return  UserService.studentRoleName;
            }else if(existingUser.getRole().getRoleName().equals(UserService.lecturerRoleName)){
                return  UserService.lecturerRoleName;
            }
            return UserService.adminRoleName;
        } else {
            return "Invalid credentials";
        }
    }

    @GetMapping("/student")
    public String student(){
        System.out.println("[[[[[");
        studentService.getFinalGrades();
        return "S+++++";
    }


    @PostMapping("/auth/register")
    public String register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String firstName = request.get("firstName");
        String lastName = request.get("lastName");
        String roleName = request.getOrDefault("role", "ROLE_STUDENT");

        if (userService.findByEmail(email) != null) {
            return "Email already exists";
        }
        System.out.println("Controller" + roleName);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        userService.saveUser(user, roleName);
        return "User registered successfully";
    }

}