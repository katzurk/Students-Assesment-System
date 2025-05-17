package JustGrades.app.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import JustGrades.app.model.Course;
import JustGrades.app.model.Student;
import JustGrades.app.repository.CourseRegistrationRepository;
import JustGrades.app.services.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private CourseRegistrationRepository courseRegistrationRepository;

    @Test
    @WithMockUser(username = "test@test.com", roles = "STUDENT")
    void getAllStudentFinalGrades_shouldReturnMap() throws Exception {
        when(studentService.getFinalGrades()).thenReturn(Map.of("bazy_danych", 4.5));

        mockMvc.perform(get("/student-info/final-grades"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bazy_danych").value(4.5));
    }

    @Test
    @WithMockUser(username = "test@student.com", roles = "STUDENT")
    void getStudentInfo_shouldReturnStudentData() throws Exception {
        Student student = new Student();
        student.setEmail("test@student.com");
        student.setStatus("ACTIVE");

        when(studentService.getStudentInfo()).thenReturn(student);

        mockMvc.perform(get("/student-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@student.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(username = "test@student.com", roles = "STUDENT")
    void getAllStudentCourses_shouldReturnList() throws Exception {
        Course course = new Course();
        course.setName("Math");
        Course course2 = new Course();
        course2.setName("BazyDanych");

        when(studentService.getAllStudentCourses()).thenReturn(List.of(course, course2));

        mockMvc.perform(get("/student-info/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Math"))
                .andExpect(jsonPath("$[1].name").value("BazyDanych"));
    }
}