package JustGrades.app.services;

import JustGrades.app.model.Role;
import JustGrades.app.model.User;
import JustGrades.app.repository.RoleRepository;
import JustGrades.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void saveUser(User userTransfer) {
        User user = new User();
        user.setFirstName(userTransfer.getFirstName());
        user.setLastName(userTransfer.getLastName());
        user.setEmail(userTransfer.getEmail());
        user.setPassword(passwordEncoder.encode(userTransfer.getPassword()));
        Role role = roleRepository.findByRoleName("ROLE_STUDENT");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
}
