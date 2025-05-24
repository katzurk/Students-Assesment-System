package JustGrades.app.controller;

import JustGrades.app.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import JustGrades.app.services.AdminService;
import JustGrades.app.services.CourseService;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final CourseService courseService;

    public AdminController(AdminService adminService, CourseService courseService) {
        this.adminService = adminService;
        this.courseService = courseService;
    }

    @PostMapping("/open-semester")
    public ResponseEntity<?> openSemester() {
        try {
            adminService.openSemester();
            return ResponseEntity.ok().body(Map.of("message", "Semester opened successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/close-semester")
    public ResponseEntity<?> closeSemester() {
        try {
            adminService.closeSemester();
            return ResponseEntity.ok().body(Map.of("message", "Semester closed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/close-all-courses")
    public ResponseEntity<Void> closeAllCourses() {
        courseService.closeAllCoursesIndividually();
        return ResponseEntity.ok().build();
    }
}

