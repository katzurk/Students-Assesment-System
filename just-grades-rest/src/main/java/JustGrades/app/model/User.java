package JustGrades.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_name")
@DiscriminatorValue("ADMIN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USERS_SEQ", allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    @NotBlank(message = "first name is mandatory")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "last name is mandatory")
    private String lastName;

    @Column(name = "email")
    @NotBlank(message = "email is mandatory")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "password is mandatory")
    private String password;

    @Transient
    @Enumerated(EnumType.STRING)
    protected Role role = Role.ADMIN;

    @Override
    public String toString() {
        return "User{" + "id=" + this.userId + ", first name=" + this.firstName + ", last name=" + this.lastName + ", password='" + this.password + '}';
    }

}
