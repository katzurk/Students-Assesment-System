package JustGrades.app.services;

import JustGrades.app.model.Lecturer;
import JustGrades.app.model.User;
import JustGrades.app.repository.LecturerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;

    public Lecturer saveLecturer(User user){
        Lecturer lecturer = new Lecturer();
        lecturer.setFirstName(user.getFirstName());
        lecturer.setLastName(user.getLastName());
        lecturer.setEmail(user.getEmail());
        lecturer.setPassword(user.getPassword());
        lecturer.setRole(user.getRole());
        return lecturerRepository.save(lecturer);
    }
}
