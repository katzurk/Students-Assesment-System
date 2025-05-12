package JustGrades.app.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @PersistenceContext
    private EntityManager entityManager;


}
