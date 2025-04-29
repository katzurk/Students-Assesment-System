package JustGrades.app.dto;

import JustGrades.app.model.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseRegisteredDTO {
    Long id;
    String name;
    Integer ects;
    String status;

    public CourseRegisteredDTO(Course course, String status) {
        this.id = course.getId();
        this.name = course.getName();
        this.ects = course.getEcts();
        this.status = status;
    }
}
