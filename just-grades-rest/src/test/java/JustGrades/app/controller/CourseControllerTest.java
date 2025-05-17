package JustGrades.app.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import JustGrades.app.exposure.CompletionRequirementInput;
import JustGrades.app.exposure.CourseInput;
import JustGrades.app.model.Course;
import JustGrades.app.repository.CourseRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    /*
        GET COURSES SORTED BY NAME ASC
     */
    @Test
    void getCoursesSortedByNameAsc_shouldReturnCourses() throws Exception {
        Course c1 = new Course();
        c1.setId(1L);
        c1.setName("Systemy operacyjne");
        c1.setEcts(5);

        Course c2 = new Course();
        c2.setId(2L);
        c2.setName("Bazy danych");
        c2.setEcts(4);

        when(courseRepository.findAllByOrderByNameAsc()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Systemy operacyjne"))
                .andExpect(jsonPath("$[1].name").value("Bazy danych"));
    }


    /*
        Delete Course
     */
    @Test
    void deleteCourse_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCourse_shouldReturnBadRequestOnException() throws Exception {
        doThrow(new DataIntegrityViolationException("Cannot delete")).when(courseRepository).deleteById(1L);

        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Can not delete course")));
    }

    /*
        ADD COURSE
     */

    @Test
    void addCourse_shouldReturnOk() throws Exception {
        CourseInput courseInput = new CourseInput();
        courseInput.setName("Physics");
        courseInput.setEcts(6);

        courseInput.setCompletionRequirements(List.of(new CompletionRequirementInput(60, "FINAL")));
        courseInput.setEnrollRequirements(List.of());

        Course course = new Course();
        course.setId(1L);
        course.setName("Physics");
        course.setEcts(6);

        when(courseRepository.save(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/addcourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(courseInput)))
                .andExpect(status().isOk())
                .andExpect(content().string("Form submitted successfully"));
    }



}
