package nl.femalert.femserver.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.femalert.femserver.model.UserStatus;
import nl.femalert.femserver.model.helper.EntityIdGenerator;
import nl.femalert.femserver.model.helper.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "[user]")
@NamedQueries({
    @NamedQuery(name = "find_all_user", query = "select U from User U"),
    @NamedQuery(name = "find_user_by_id", query = "select U from User U where U.id = ?1"),
    @NamedQuery(name = "find_user_by_name", query = "select U from User U where U.name = ?1"),
    @NamedQuery(name = "find_user_by_email_address", query = "select U from User U where U.emailAddress = ?1")
})
public class User implements Identifiable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "_seq_user")
    @GenericGenerator(
        name = "_seq_user",
        strategy = "nl.femalert.femserver.model.helper.EntityIdGenerator",
        parameters = {
            @Parameter(name = EntityIdGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = EntityIdGenerator.VALUE_PREFIX_PARAMETER, value = "USR-"),
            @Parameter(name = EntityIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d")
    })
    private String id;

    @Column(name = "name")
    private String name;

    @Email
    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "admin", nullable = false)
    private boolean admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    protected User() {}

    public User(String id) {
        this.id = id;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
            "User{ id: '%s', name: '%s', emailAddress: '%s', admin: '%s' }",
            id,
            name,
            emailAddress,
            admin
        );
    }
}
