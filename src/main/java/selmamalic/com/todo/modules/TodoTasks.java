package main.java.selmamalic.com.todo.modules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.selmamalic.com.todo.Utilities.HibernateUtil;
import main.java.selmamalic.com.todo.login.Login;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class TodoTasks {

    private static final TodoTasks instance = new TodoTasks();

    private ObservableList<Tasks> tasksObservableList;
    private ObservableList<Tasks> refreshedList;

    public static TodoTasks getInstance(){
        return instance;
    }

    public ObservableList<Tasks> getTasks() {
        return this.tasksObservableList;
    }

    public ObservableList<Tasks> getRefreshedList() {
        return refreshedList;
    }


    public void loadTasks(){
        Transaction transaction = null;
        String hql = "from Tasks  where  user_ID= :id";
        tasksObservableList = FXCollections.observableArrayList();
        try (Session session = HibernateUtil.createSessionFactory().openSession()){
            transaction = session.beginTransaction();
            List<Tasks> taskList = session.createQuery(hql, Tasks.class).setParameter("id", Login.getUserID()).getResultList();
            for (Tasks tasks: taskList) {
                int taskId = tasks.getTaskID();
                String description = tasks.getDescription();
                String taskName = tasks.getTask();
                LocalDate taskDate = tasks.getDate();

                Tasks tasks1 = new Tasks(taskId, taskName, description, taskDate);
                tasksObservableList.add(tasks1);
            }

            transaction.commit();
        }catch (HibernateException e){
            if (transaction != null) {
                transaction.rollback();
            }
        }finally {
            HibernateUtil.close();
        }
    }

    public void refresh(){
        Transaction transaction = null;
        String hql = "from Tasks  where  user_ID= :id";
        List<Tasks> taskList;

        try (Session session = HibernateUtil.createSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Query<Tasks> query = session.createQuery(hql, Tasks.class);
            query.setParameter("id", Login.getUserID());
            taskList = query.getResultList();
            refreshedList = FXCollections.observableArrayList();
            for (Tasks tasks: taskList) {
                int taskId = tasks.getTaskID();
                String description = tasks.getDescription();
                String taskName = tasks.getTask();
                LocalDate taskDate = tasks.getDate();

                Tasks tasks1 = new Tasks(taskId, taskName, description, taskDate);
                refreshedList.add(tasks1);
            }
            transaction.commit();
        }catch (HibernateException e){
            if (transaction != null) {
                transaction.rollback();
            }
        }finally {
            HibernateUtil.close();
        }
    }


    public void saveTask(String taskTitle, Timestamp timestamp, String taskDescription, LocalDate date){
        Transaction transaction = null;
        try (Session session = HibernateUtil.createSessionFactory().openSession()){
            transaction = session.beginTransaction();
            User user = session.get(User.class, Login.getUserID());
            Tasks tasks = new Tasks();
            tasks.setUserData(user);
            tasks.setTask(taskTitle);
            tasks.setDateCreated(timestamp);
            tasks.setDescription(taskDescription);
            tasks.setDate(date);
            session.save(tasks);
            transaction.commit();

        } catch (HibernateException e){
            if (transaction != null) {
                transaction.rollback();
            }
        }finally {
            HibernateUtil.close();
        }
    }

    public void updateTask(String taskName, Timestamp dateCreated, String description, LocalDate date, int taskId){
        Transaction transaction = null;

        try (Session session = HibernateUtil.createSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Tasks tasks = session.get(Tasks.class, taskId);
            tasks.setTask(taskName);
            tasks.setDateCreated(dateCreated);
            tasks.setDescription(description);
            tasks.setDate(date);
            session.update(tasks);
            transaction.commit();
        } catch (HibernateException e){
            if (transaction != null) {
                transaction.rollback();
            }
        }finally {
            HibernateUtil.close();
        }
    }

    public void deleteTask(Tasks tasks, int taskId){
        tasksObservableList.remove(tasks);
        Transaction transaction = null;
        try (Session session = HibernateUtil.createSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Query<Tasks> query = session.createQuery("DELETE FROM Tasks AS task WHERE taskID=:taskID AND user_ID=:id");
            query.setParameter("taskID", taskId);
            query.setParameter("id", Login.getUserID());
            query.executeUpdate();

            transaction.commit();

        } catch (HibernateException e){
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            HibernateUtil.close();
        }

    }


}
