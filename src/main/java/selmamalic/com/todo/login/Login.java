package main.java.selmamalic.com.todo.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.selmamalic.com.todo.Utilities.HibernateUtil;
import main.java.selmamalic.com.todo.modules.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {

    public static String firstName;
    public static int userID;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane signupAndExitPane;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private JFXButton exitBtn;

    @FXML
    private JFXButton signupBtn;

    @FXML
    private JFXButton loginBtn;

    @FXML
    private JFXTextField usernameFld;

    @FXML
    private JFXPasswordField passwordFld;

    @FXML
    void exit(ActionEvent event) {
        if (event.getSource() == exitBtn) {
            Platform.exit();
        }
    }

    @FXML
    void login(ActionEvent event) {
        if (event.getSource() == loginBtn) {
            loginForm();
        }
    }

    @FXML
    void signup(ActionEvent event) {
        if (event.getSource() == signupBtn) {
            signupForm();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private boolean loginForm() {
        User user = null;
        String username = usernameFld.getText();
        String pass = passwordFld.getText();

        if (username.isEmpty() & pass.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("You didn't type anything!");
            alert.setContentText("Please enter your Username and Password!");
            alert.showAndWait();
        } else {
            Transaction transaction = null;
            try (Session session = HibernateUtil.createSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                user = (User) session.createQuery("from User user where user.username = :username")
                        .setParameter("username", usernameFld.getText())
                        .uniqueResult();

                userID = user.getID();
                firstName = user.getFirstName();

                if (user != null && user.getPassword().equals(passwordFld.getText())) {
                    addAndShowTaskForm();
                    return true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Ups!!");
                    alert.setContentText("Username or password isn't correct");
                    alert.showAndWait();

                    usernameFld.clear();
                    passwordFld.clear();
                }
                transaction.commit();
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                System.out.println(e);
            } finally {
                HibernateUtil.close();
            }
        }
        return false;
    }

    private void addAndShowTaskForm() {
        rootPane.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addAndShowTaskForm.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(new Scene(parent, 1000, 667));

        AddAndShowTaskForm addAndShowTaskForm = loader.getController();
        addAndShowTaskForm.setUserID(userID);

        stage.show();
    }

    private void signupForm() {
        rootPane.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../signup/signupForm.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setScene(new Scene(parent, 700, 500));
        stage.setResizable(false);

        stage.show();
    }
    public static int getUserID() {
        return userID;
    }
}
