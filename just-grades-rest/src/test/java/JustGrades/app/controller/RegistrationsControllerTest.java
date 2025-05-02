package JustGrades.app.controller;

import JustGrades.app.dto.CourseRegisteredDTO;
import JustGrades.app.model.CourseRegistration;
import JustGrades.app.services.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationsController.class)
class RegistrationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    /*
        GET ALL OPENED COURSES
     */
    @Test
    @WithMockUser(username = "test@test.com", roles = "STUDENT")
    void getAllOpenedCourses_shouldReturnList() throws Exception {
        CourseRegisteredDTO dto = new CourseRegisteredDTO();
        dto.setId(1L);
        dto.setName("BazyDanych");
        dto.setStatus("ACTIVE");

        when(registrationService.getCourseRegistered()).thenReturn(List.of(dto));

        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("BazyDanych"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }
    @Test
    void getAllOpenedCourses_shouldReturnUnauthorized_whenNoUser() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isUnauthorized());
    }

    /*
        REGISTER FOR COURSE
     */
    @Test
    @WithMockUser(username = "test@test.com", roles = "STUDENT")
    void registerForCourse_shouldReturnSuccessMessage() throws Exception {
        when(registrationService.registerForCourse(1L)).thenReturn(new CourseRegistration());

        mockMvc.perform(post("/registration/register")
                        .param("courseId", "1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Registered successfully."));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "STUDENT")
    void registerForCourse_shouldReturnForbidden_whenNoCsrf() throws Exception {
        mockMvc.perform(post("/registration/register")
                        .param("courseId", "1"))
                .andExpect(status().isForbidden());
    }


    /*
        DEREGISTER FROM COURSE
     */
    @Test
    @WithMockUser(username = "test@test.com", roles = "STUDENT")
    void deregisterFromCourse_shouldReturnSuccessMessage() throws Exception {
        when(registrationService.deregisterFromCourse(1L)).thenReturn(new CourseRegistration());

        mockMvc.perform(post("/registration/deregister")
                        .param("courseId", "1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Deregistered successfully."));
    }

}