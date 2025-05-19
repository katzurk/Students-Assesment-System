package JustGrades.app.controller;

import JustGrades.app.model.Role;
import JustGrades.app.model.User;
import JustGrades.app.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserService userService;

    @Test
    void login_success() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@test.com");
        mockUser.setPassword("password");
        mockUser.setRole(Role.STUDENT);

        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(userService.findByEmail("test@test.com")).thenReturn(mockUser);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@test.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("STUDENT"));
    }

    @Test
    void login_invalidCredentials() throws Exception {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Wrong"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"wrong@test.com\", \"password\": \"123\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_missingEmailOrPassword() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"\", \"password\": \"\"}"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void login_userNotFound() throws Exception {
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(userService.findByEmail("notfound@test.com")).thenReturn(null);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"notfound@test.com\", \"password\": \"any\"}"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void register_newUser_shouldReturnSuccessMessage() throws Exception {
        User newUser = new User();
        newUser.setEmail("new@test.com");
        newUser.setPassword("password");

        when(userService.findByEmail("new@test.com")).thenReturn(null);
        when(userService.saveUser(any(User.class), any())).thenReturn(newUser);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "new@test.com",
                          "password": "password",
                          "firstName": "Anna",
                          "lastName": "Nowak",
                          "role": "STUDENT"
                        }
                    """))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void register_userAlreadyExists_shouldReturnConflict() throws Exception {
        User existingUser = new User();
        existingUser.setEmail("existing@test.com");
        when(userService.findByEmail("existing@test.com")).thenReturn(existingUser);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "existing@test.com",
                          "password": "password",
                          "role": "STUDENT"
                        }
                    """))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email already exists"));
    }

    @Test
    void logout_shouldRedirectOrReturnDefaultResponse() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection());
    }

}
