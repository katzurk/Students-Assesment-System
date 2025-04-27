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
    Course course;
    Boolean registered;
}
