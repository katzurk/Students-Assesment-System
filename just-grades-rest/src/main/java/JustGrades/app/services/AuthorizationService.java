package JustGrades.app.services;


import JustGrades.app.model.User;
import JustGrades.app.repository.UserRepository;


import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        System.out.println("+++++++");
        User user = userRepository.findByEmail(usernameOrEmail);
        System.out.println(user.getUserId());
        System.out.println(user.getFirstName());
        System.out.println(user.getRole().getRoleName());
        if (user != null) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(String.valueOf(new SimpleGrantedAuthority(user.getRole().getRoleName())))
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid email or password");
        }
    }
}