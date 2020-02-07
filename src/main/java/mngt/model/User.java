package mngt.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import mngt.repository.validation.EntityExist;
import mngt.util.DeserializerRoleJsonConverter;
import mngt.util.SerializerRoleJsonConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "users")
public class User implements Serializable {

    //Static parameters
    public static final String ROLE_VALIDATION_MESSAGE = "There is no such role(s)";
    public static final String PASSWORD_VALIDATION_MESSAGE = "Password must contain one uppercase letter and one digit";

    private static final long serialVersionUID = 1L;

    //Properties
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Basic
    private String name;

    @NotNull
    @Basic
    private String login;

    @NotNull
    @Pattern(regexp = "(\\S*\\p{Upper}+\\S*\\d+\\S*)|(\\S*\\d+\\S*\\p{Upper}+\\S*)",
            message = PASSWORD_VALIDATION_MESSAGE)
    @Basic
    private String password;

    @EntityExist(message = ROLE_VALIDATION_MESSAGE)
    @JsonDeserialize(contentConverter = DeserializerRoleJsonConverter.class)
    @JsonSerialize(contentConverter = SerializerRoleJsonConverter.class)
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    //Constructors
    public User() {
    }

    public User(String name, String login, String password, List<Role> roles) {
        this(0, name, login, password, roles);
    }

    public User(long id, String name, String login, String password, List<Role> roles) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    //Getter and Setters
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    //Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, login, password, roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + "hidden" + '\'' +
                ", roles=" + roles +
                '}';
    }
}
