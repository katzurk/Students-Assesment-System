package JustGrades.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @Column(nullable = false, unique = true)
    private String roleName;
    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
