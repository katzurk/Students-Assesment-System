package JustGrades.app.controller;

import JustGrades.app.exposure.GradeInput;
import JustGrades.app.model.Course;
import JustGrades.app.model.Grade;
import JustGrades.app.model.Student;
import JustGrades.app.repository.CourseRepository;
import JustGrades.app.repository.GradeRepository;
import JustGrades.app.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GradeRepository gradeRepository;

    @MockitoBean
    private StudentRepository studentRepository;

    @MockitoBean
    private CourseRepository courseRepository;

    /*
        GET GRADES LIST
     */
    @Test
    @WithMockUser(username = "test@test.com", roles = "LECTURER")
    void getGradesList_shouldReturnGrades() throws Exception {
        Grade g = new Grade();
        g.setGrade(5);
        g.setStudent(new Student());
        when(gradeRepository.findByCourseIdOrderByStudentLastNameAsc(1L)).thenReturn(List.of(g));

        mockMvc.perform(get("/course/1/grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].grade").value(5));
    }

    @Test
    @WithMockUser
    void deleteGrade_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/course/1/grades/100"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "LECTURER")
    void addGrade_shouldReturnSuccess() throws Exception {
        GradeInput input = new GradeInput();
        input.setGrade(4);
        input.setStudentNumber("12345");
        input.setType("FINAL");

        Student s = new Student();
        Course c = new Course();
        when(courseRepository.findById(1L)).thenReturn(Optional.of(c));
        when(studentRepository.findStudentByStudentNumber("12345")).thenReturn(s);
        when(gradeRepository.save(any(Grade.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/course/1/addgrades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "grade": 4,
                        "studentNumber": "12345",
                        "type": "FINAL"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(content().string("Form submitted successfully"));
    }
}
