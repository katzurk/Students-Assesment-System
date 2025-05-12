package JustGrades.app.services;

import JustGrades.app.repository.UserRepository;
import JustGrades.app.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void openSemester() {
        adminRepository.openSemester();
    }

    public void closeSemester() {
        adminRepository.closeSemester();
    }

    public void closeAllCourses() {
        adminRepository.closeAllCourses();
    }
}
