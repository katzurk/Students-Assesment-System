package JustGrades.app.controller;

import JustGrades.app.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataAccessException;
import java.util.Map;



@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/open-semester")
    public String openSemester() {
        try {
            adminService.openSemester();
            return "Semester opened successfully";
        } catch (DataAccessException ex) {
            Throwable root = ex.getRootCause();
            return root != null ? root.getMessage() : "Unknown database error";
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

    @PostMapping("/close-semester")
    public String closeSemester() {
        try {
            adminService.closeSemester();
            return "Semester closed successfully";
        } catch (DataAccessException ex) {
            Throwable root = ex.getRootCause();
            return root != null ? root.getMessage() : "Unknown database error";
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

    @PostMapping("/close-all-courses")
    public String closeAllCourses() {
        try {
            adminService.closeAllCourses();
            return "All courses closed successfully";
        } catch (DataAccessException ex) {
            Throwable root = ex.getRootCause();
            return root != null ? root.getMessage() : "Unknown database error";
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

}
