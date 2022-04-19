package main.java.selmamalic.com.todo.modules;

import main.java.selmamalic.com.todo.Utilities.Const;

import javax.persistence.*;
import java.util.List;

@Entity(name = Const.USER_ENTITY)
@Table(name = Const.USERS_TABLE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Const.USERS_ID)
    private int ID;

    @Column(name = Const.USERS_FIRSTNAME)
    private String firstName;

    @Column(name = Const.USERS_LASTNAME)
    private String lastName;

    @Column(name = Const.USERS_USERNAME, nullable = false, unique = true)
    private String username;

    @Column(name = Const.USERS_PASSWORD)
    private String password;

    @Column(name = Const.USERS_GENDER)
    private String gender;

    @Column(name = Const.USERS_ADDRESS)
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tasks> tasks;

    public User() {}

    public int getID() { return ID; }

    public void setID(int ID) { this.ID = ID;}

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName;}

    public String getLastName() { return lastName;}

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() {return username;}

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getGender() {return gender;}

    public void setGender(String gender) {this.gender = gender;}

    public List<Tasks> getTasks() { return this.tasks;}

    public void setTasks(List<Tasks> tasks) {this.tasks = tasks;}

    public User(String firstName, String lastName, String username, String password, String gender, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.address = address;
    }
}
