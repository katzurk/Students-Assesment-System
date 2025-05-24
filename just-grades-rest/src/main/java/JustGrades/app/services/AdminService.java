package JustGrades.app.services;

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
        try {
            adminRepository.openSemester();
        } catch (Exception e) {
            throw new RuntimeException("Cannot open semester: " + extractOracleErrorMessage(e), e);
        }
    }

    public void closeSemester() {
        try {
            adminRepository.closeSemester();
        } catch (Exception e) {
            throw new RuntimeException("Cannot close semester: " + extractOracleErrorMessage(e), e);
        }
    }


    private String extractOracleErrorMessage(Throwable e) {
        Throwable current = e;
        while (current != null) {
            String msg = current.getMessage();
            if (msg != null && msg.contains("ORA-")) {
                int colonIndex = msg.indexOf(':');
                if (colonIndex != -1 && colonIndex + 1 < msg.length()) {
                    String mainMessage = msg.substring(colonIndex + 1).trim();
                    int tailIndex = mainMessage.indexOf("ORA-06512");
                    if (tailIndex != -1) {
                        return mainMessage.substring(0, tailIndex).trim();
                    }
                    tailIndex = mainMessage.indexOf("] [n/a]");
                    if (tailIndex != -1) {
                        return mainMessage.substring(0, tailIndex).trim();
                    }
                    return mainMessage;
                } else {
                    return msg.trim();
                }
            }
            current = current.getCause();
        }
        return e.getMessage();
    }

}

