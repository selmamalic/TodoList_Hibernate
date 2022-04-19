package main.java.selmamalic.com.todo.signup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.selmamalic.com.todo.Utilities.HibernateUtil;
import main.java.selmamalic.com.todo.modules.Data;
import main.java.selmamalic.com.todo.modules.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

public class SignupForm {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton createAccountBtn;

    @FXML
    private JFXTextField firstnameTxt;

    @FXML
    private JFXTextField lastnameTxt;

    @FXML
    private JFXTextField usernameTxt;

    @FXML
    private JFXTextField addressTxt;

    @FXML
    private JFXPasswordField passwordTxt;

    @FXML
    private JFXPasswordField confirmPasswordTxt;

    @FXML
    private JFXButton backToLoginBtn;

    @FXML
    private JFXCheckBox maleCheckBox;

    @FXML
    private JFXCheckBox femaleCheckBox;

    @FXML
    void backToLoginPage(ActionEvent event) {
        if (event.getSource() == backToLoginBtn) {
            showForm("../login/login.fxml");
        }
    }

    @FXML
    void createAccount(ActionEvent event) {
        if (event.getSource() == createAccountBtn) {
            createUser();
        }
    }

    private void createUser() {
        String firstname = firstnameTxt.getText();
        String lastname = lastnameTxt.getText();
        String username = usernameTxt.getText();
        String address = addressTxt.getText();
        String pass = passwordTxt.getText();
        String confirmPass = confirmPasswordTxt.getText();
        String gender;
        if (maleCheckBox.isSelected()) {
            gender = "Male";
        } else if (femaleCheckBox.isSelected()) {
            gender = "Female";
        } else {
            gender = "";
        }

        if (firstname.isEmpty() & lastname.isEmpty() & username.isEmpty()
                & address.isEmpty() & gender.isEmpty() & pass.isEmpty()
                & confirmPass.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something is missing!");
            alert.setContentText("Make sure you have entered all the data!");
            alert.showAndWait();
            return;
        } else {
            if (pass.equals(confirmPass)) {
                Transaction transaction = null;
                String hql = "from User where username = :userName";
                try (Session session = HibernateUtil.createSessionFactory().openSession()) {
                    transaction = session.beginTransaction();
                    User user = (User) session.createQuery(hql).setParameter("userName", username).uniqueResult();
                    if (user != null && user.getUsername().equals(username)) {
                        //user exist
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Ups!!");
                        alert.setContentText("Username is already taken!");
                        alert.showAndWait();
                    } else {
                        //user doesn't exist
                        Data data = new Data();
                        data.insertUser(firstname, lastname, username, pass, gender, address);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("User successfully created");
                        alert.setContentText("Please login with your username and password");
                        alert.showAndWait();

                        showForm("../login/login.fxml");
                    }
                    transaction.commit();
                } catch (HibernateException e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    e.printStackTrace();
                } finally {
                    HibernateUtil.close();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Passwords don't match!");
                alert.showAndWait();
                return;
            }
        }

    }


    private void showForm(String url) {
        root.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(url));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(new Scene(parent, 700, 500));

        stage.show();
    }

}
