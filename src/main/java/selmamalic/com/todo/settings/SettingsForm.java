package main.java.selmamalic.com.todo.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.selmamalic.com.todo.login.AddAndShowTaskForm;
import main.java.selmamalic.com.todo.modules.Data;
import main.java.selmamalic.com.todo.modules.User;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SettingsForm implements Initializable {

    Data data = new Data();

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton updateDataBtn;

    @FXML
    private JFXTextField firstNameTxt;

    @FXML
    private JFXTextField lastNameTxt;

    @FXML
    private JFXTextField userNameTxt;

    @FXML
    private JFXTextField addressTxt;

    @FXML
    private JFXPasswordField passwordTxt;

    @FXML
    private JFXPasswordField confirmPasswordTxt;

    @FXML
    private JFXButton backBtn;

    @FXML
    private JFXCheckBox maleCheckBox;

    @FXML
    private JFXCheckBox femaleCheckBox;

    @FXML
    private JFXButton changePasswordBtn;

    @FXML
    private JFXButton deleteAccountBtn1;

    @FXML
    void backToAddAndShowTasks(ActionEvent event) {
        if (event.getSource() == backBtn) {
            showForm("../login/addAndShowTaskForm.fxml",1000, 667);
        }
    }

    @FXML
    void changePassword(ActionEvent event) {
        if (event.getSource() == changePasswordBtn) {
            changePass();
        }
    }

    @FXML
    void deleteAccount(ActionEvent event) {
        if (event.getSource() == deleteAccountBtn1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Todo Item");
            alert.setHeaderText("Delete user account!");
            alert.setContentText("Are you sure?  Press OK to confirm, or cancel to Back out.");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && (result.get() == ButtonType.OK)) {
                data.deleteUser(AddAndShowTaskForm.getUserID());
                showForm("../login/login.fxml",700, 500);
            }
        }
    }

    @FXML
    void saveUpdatedData(ActionEvent event) {
        if (event.getSource() == updateDataBtn) {
            updateUserData();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userData();
    }

    private void userData(){
        User user = data.userData();
        firstNameTxt.setText(user.getFirstName());
        lastNameTxt.setText(user.getLastName());
        userNameTxt.setText(user.getUsername());
        addressTxt.setText(user.getAddress());

        if (user.getGender().equals("Male")){
            maleCheckBox.setSelected(true);
        } else if (user.getGender().equals("Female")){
            femaleCheckBox.setSelected(true);
        }
    }

    private void updateUserData() {
        String firstname = firstNameTxt.getText();
        String lastname = lastNameTxt.getText();
        String username = userNameTxt.getText();
        String address = addressTxt.getText();
        String gender;
        if (maleCheckBox.isSelected()) {
            gender = "Male";
        } else if (femaleCheckBox.isSelected()) {
            gender = "Female";
        } else {
            gender = "";
        }

        if (firstname.isEmpty() & lastname.isEmpty() & username.isEmpty()
                & address.isEmpty() & gender.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something is missing!");
            alert.setContentText("Make sure you have entered all the data!");
            alert.showAndWait();
        } else {
            data.updateUser(firstname, lastname, username, gender, address, AddAndShowTaskForm.getUserID());
        }
    }

    private void changePass() {
        String pass = passwordTxt.getText();
        String checkPass = confirmPasswordTxt.getText();
        if (checkPass.equals(pass)){
            data.updatePassword(pass, AddAndShowTaskForm.getUserID());
        }
    }

    private void showForm(String url, int width, int height) {
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
        stage.setScene(new Scene(parent, width, height));

        stage.show();
    }



}
