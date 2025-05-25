package JustGrades.app.services;

import JustGrades.app.repository.CourseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public void closeAllCoursesIndividually() {
        List<Long> courseIds = courseRepository.findCoursesToClose();

        for (Long courseId : courseIds) {
            String status = (String) entityManager
                    .createNativeQuery("SELECT status FROM courses WHERE course_id = :id")
                    .setParameter("id", courseId)
                    .getSingleResult();

            if ("opened registration".equalsIgnoreCase(status)) {
                entityManager.createNativeQuery("BEGIN close_registration(:id); END;")
                        .setParameter("id", courseId)
                        .executeUpdate();
            }

            else {
                entityManager.createNativeQuery("BEGIN close_course(:id); END;")
                        .setParameter("id", courseId)
                        .executeUpdate();
            }
        }
    }
}

