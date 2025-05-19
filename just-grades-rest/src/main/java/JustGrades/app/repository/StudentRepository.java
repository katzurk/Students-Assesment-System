package JustGrades.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import JustGrades.app.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT get_final_grade(:studentId, :courseId) FROM dual", nativeQuery = true)
    Optional<Double> getFinalGrade(@Param("studentId")  Long studentId, @Param("courseId") Long courseId);

    Student findByEmail(String email);

    Student findStudentByUserId(long l);

    Student findStudentByStudentNumber(String studentNumber);
}
