package main.java.selmamalic.com.todo.modules;

import main.java.selmamalic.com.todo.Utilities.Const;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = Const.TASKS_TABLE)
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Const.TASKS_ID)
    private int taskID;

    @Column(name = Const.TASKS_TASK)
    private String task;

    @Column(name = Const.TASKS_DATE_CREATED)
    private Timestamp dateCreated;

    @Column(name = Const.TASKS_DESCRIPTION)
    private String description;

    @Column(name = Const.TASKS_DATE)
    private LocalDate date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = Const.TASK_USER)
    private User user;


    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }


    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public User getUserData() {
        return user;
    }

    public void setUserData(User user) {
        this.user = user;
    }

    public Tasks() {}

    public Tasks(int taskID, String task, Timestamp dateCreated, String description, LocalDate date) {
        this.taskID = taskID;
        this.task = task;
        this.dateCreated = dateCreated;
        this.description = description;
        this.date = date;
    }

    public Tasks(String task, Timestamp dateCreated, String description, LocalDate date) {
        this.task = task;
        this.dateCreated = dateCreated;
        this.description = description;
        this.date = date;
    }

    public Tasks(int t_id ,String task, String description, LocalDate date) {
        this.taskID = t_id;
        this.task = task;
        this.description = description;
        this.date = date;
    }
}
