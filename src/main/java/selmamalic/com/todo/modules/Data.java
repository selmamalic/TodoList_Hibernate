package main.java.selmamalic.com.todo.modules;

import javafx.scene.control.Alert;
import main.java.selmamalic.com.todo.Utilities.HibernateUtil;
import main.java.selmamalic.com.todo.login.AddAndShowTaskForm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class Data {

    private Query query;
    private Alert alert;

    public void insertUser(String firstname, String lastname, String username,
                           String password, String gender, String address) {
        User user = new User();
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(gender);
        user.setAddress(address);
        Transaction transaction = null;
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            HibernateUtil.close();
        }
    }

    public void updateUser(String firstname, String lastname, String username, String gender, String address, int id) {

        Transaction transaction = null;

        String hql = "update User user set firstName = :firstname, lastName = :lastname, username = :username,"
                + " gender = :gender, address = :address where ID = :userid";

        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            query = session.createQuery(hql);
            query.setParameter("firstname",firstname);
            query.setParameter("lastname", lastname);
            query.setParameter("username", username);
            query.setParameter("gender", gender);
            query.setParameter("address", address);
            query.setParameter("userid", id);
            int result = query.executeUpdate();
            if (result >= 0){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Data successfully updated");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Something missing!");
                alert.showAndWait();
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            HibernateUtil.close();
        }
    }

    public void updatePassword(String password, int id){
        Transaction transaction = null;

        String hql = "update User set password=:password where ID=:userid";

        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            query = session.createQuery(hql);
            query.setParameter("password", password);
            query.setParameter("userid", id);
            int result = query.executeUpdate();
            if (result >= 0){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Password successfully updated");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Something missing!");
                alert.showAndWait();
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            HibernateUtil.close();
        }

    }

    public void deleteUser(int id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            List<Tasks> tasks = session.createQuery("from Tasks  where  user_ID= :id", Tasks.class).setParameter("id", id).list();
            for (Tasks tasks1 :tasks){
                session.delete(tasks1);
            }
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            HibernateUtil.close();
        }
    }

    public User userData(){
        User user = null;
        Transaction transaction = null;
        try (Session session = HibernateUtil.createSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            user = session.get(User.class, AddAndShowTaskForm.getUserID());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            HibernateUtil.close();
        }
        return user;
    }
}


