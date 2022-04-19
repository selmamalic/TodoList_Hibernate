package main.java.selmamalic.com.todo.login;

import com.jfoenix.controls.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.selmamalic.com.todo.modules.Tasks;
import main.java.selmamalic.com.todo.modules.TodoTasks;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.function.Predicate;

public class AddAndShowTaskForm {

    public static int userID;

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane createTaskPane;

    @FXML
    private JFXButton saveTaskBtn;

    @FXML
    private JFXTextArea shortDescriptionFld;

    @FXML
    private JFXTextField taskNameFld;

    @FXML
    private JFXDatePicker chooseDayPicker;

    @FXML
    private Label taskSavedOrNotLabel;

    @FXML
    private AnchorPane MyTasksPane;

    @FXML
    private JFXListView<Tasks> tasksList;

    @FXML
    private JFXTextArea tasksShortDescriptionTextBox;

    @FXML
    private JFXToolbar tollbar;

    @FXML
    private JFXToggleButton todaysTasksToggleBtn;

    @FXML
    private JFXButton refreshBtn;

    @FXML
    private Label dateLbl;

    @FXML
    private JFXButton settingsBtn;

    @FXML
    private JFXButton logoutBtn;

    @FXML
    private Label helloLabel;

    @FXML
    void backToTheLoginPage(ActionEvent event) {
        if (event.getSource() == logoutBtn) {
            showForm("../login/login.fxml");
        }
    }

    @FXML
    void refreshList(ActionEvent event) {
        if (event.getSource() == refreshBtn) {
            refreshedList();
        }
    }

    @FXML
    void saveTask(ActionEvent event) {
        if (event.getSource() == saveTaskBtn) {
            saveUserTask();
        }
    }

    @FXML
    void settings(ActionEvent event) {
        if (event.getSource() == settingsBtn) {
            showForm("../settings/settingsForm.fxml");
        }
    }

    SortedList<Tasks> sortedList;
    FilteredList<Tasks> tasksFilteredList;
    Predicate<Tasks> wantAllItems;
    Predicate<Tasks> wantTodayTasks;

    @FXML
    void initialize() {

        helloLabel.setText(" Welcome " + Login.firstName);

        TodoTasks.getInstance().loadTasks();

        tasksList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        Tasks item = tasksList.getSelectionModel().getSelectedItem();
                        tasksShortDescriptionTextBox.setText(item.getDescription());
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd . MM . yyyy");
                        dateLbl.setText("Due: " + df.format(item.getDate()));
                        if (item.getDate().isBefore(LocalDate.now())) {
                            dateLbl.setTextFill(Color.RED);
                        } else {
                            dateLbl.setTextFill(Color.GREEN);
                        }
                    }
                });

        wantAllItems = tasks -> true;
        wantTodayTasks = tasks -> (tasks.getDate().equals(LocalDate.now()));

        tasksFilteredList = new FilteredList<>(TodoTasks.getInstance().getTasks(), wantAllItems);

        sortedList = new SortedList<>(tasksFilteredList, Comparator.comparing(Tasks::getDate));

        tasksList.setItems(sortedList);
        tasksList.setCellFactory(CellController -> new CellController());
    }

    @FXML
    void getTodaysTasks(MouseEvent event) {
        if (event.getSource() == todaysTasksToggleBtn) {
            Tasks selectedItem = tasksList.getSelectionModel().getSelectedItem();
            if(todaysTasksToggleBtn.isSelected()) {
                tasksFilteredList.setPredicate(wantTodayTasks);
                if(tasksFilteredList.isEmpty()) {
                    tasksShortDescriptionTextBox.clear();
                    dateLbl.setText("");
                } else if(tasksFilteredList.contains(selectedItem)) {
                    tasksList.getSelectionModel().select(selectedItem);
                } else {
                    tasksList.getSelectionModel().selectFirst();
                }
            } else {
                tasksFilteredList.setPredicate(wantAllItems);
                tasksList.getSelectionModel().select(selectedItem);
            }
        }
    }


    public void refreshedList() {
        TodoTasks.getInstance().refresh();
        tasksList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        Tasks item = tasksList.getSelectionModel().getSelectedItem();
                        tasksShortDescriptionTextBox.setText(item.getDescription());
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd . MM . yyyy");
                        dateLbl.setText("Due: " + df.format(item.getDate()));
                        if (item.getDate().isBefore(LocalDate.now())) {
                            dateLbl.setTextFill(Color.RED);
                        } else {
                            dateLbl.setTextFill(Color.GREEN);
                        }
                    }
                });

        wantAllItems = tasks -> true;
        wantTodayTasks = tasks -> (tasks.getDate().equals(LocalDate.now()));

        tasksFilteredList = new FilteredList<>(TodoTasks.getInstance().getRefreshedList(), wantAllItems);

        sortedList = new SortedList<>(tasksFilteredList, Comparator.comparing(Tasks::getDate));

        tasksList.setItems(sortedList);
        tasksList.setCellFactory(CellController -> new CellController());
    }

    private void saveUserTask() {
        String taskTitle = taskNameFld.getText();
        String taskDescription = shortDescriptionFld.getText();
        LocalDate date = chooseDayPicker.getValue();
        if (taskTitle.isEmpty() & taskDescription.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something is missing!");
            alert.setContentText("Make sure you have entered all the data!");
            alert.showAndWait();
        } else {
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
            TodoTasks.getInstance().saveTask(taskTitle, timestamp, taskDescription, date);
            if (true) {
                taskSavedOrNotLabel.setTextFill(Color.GREEN);
                taskSavedOrNotLabel.setText("Task added");

                taskNameFld.clear();
                shortDescriptionFld.clear();
                chooseDayPicker.setPromptText("00.00.0000");
            } else {
                taskSavedOrNotLabel.setTextFill(Color.RED);
                taskSavedOrNotLabel.setText("Task did not added");
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

    public static int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        AddAndShowTaskForm.userID = userID;
    }
}
