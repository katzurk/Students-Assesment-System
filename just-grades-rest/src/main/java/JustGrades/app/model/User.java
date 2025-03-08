package JustGrades.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
    @SequenceGenerator(name = "userSeq", sequenceName = "USERS_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "first_name")
    @NotBlank(message = "first name is mandatory")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "last name is mandatory")
    private String lastName;

    private String email;

    @Column(name = "password_hash")
    @NotBlank(message = "password is mandatory")
    private String passwordHash;

    public User() {
    }

    public User(String firstName, String lastName, String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
    }


    public long getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPasswordHash(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + this.id + ", first name=" + this.firstName + ", last name=" + this.lastName + ", password='" + this.passwordHash + '}';
    }
}
