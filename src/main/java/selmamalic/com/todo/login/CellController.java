package main.java.selmamalic.com.todo.login;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.selmamalic.com.todo.modules.Tasks;
import main.java.selmamalic.com.todo.modules.TodoTasks;
import main.java.selmamalic.com.todo.updateTask.UpdateTask;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

public class CellController extends JFXListCell<Tasks> {

    private FXMLLoader fxmlLoader;

    @FXML
    private AnchorPane cellRoot;

    @FXML
    private Label cellLabel;

    @FXML
    private ImageView updateImg;

    @FXML
    private ImageView deleteImg;



    @Override
    protected void updateItem(Tasks tasks, boolean empty) {
        super.updateItem(tasks, empty);
        if (empty || tasks == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("listCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cellLabel.setText(tasks.getTask());
            if (tasks.getDate().isBefore(LocalDate.now())) {
                cellLabel.setTextFill(Color.RED);
            } else if (tasks.getDate().isAfter(LocalDate.now())) {
                cellLabel.setTextFill(Color.BLUE);
            } else {
                cellLabel.setTextFill(Color.GREEN);
            }
            updateImg.setOnMouseMoved(event -> {
                Tooltip tooltip = new Tooltip("Update task");
                tooltip.setFont(Font.font("Monotype Corsiva", 15.0));
                tooltip.setStyle("-fx-background-color:  #bf5f82; -fx-border-radius: 10 10 10 10;");
                Tooltip.install(updateImg, tooltip);
            });

            deleteImg.setOnMouseMoved(event -> {
                Tooltip tooltip = new Tooltip("Delete task");
                tooltip.setFont(Font.font("Monotype Corsiva", 15.0));
                tooltip.setStyle("-fx-background-color:  #bf5f82; -fx-border-radius: 10 10 10 10;");
                Tooltip.install(deleteImg, tooltip);
            });
            updateImg.setOnMouseClicked(event -> {
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../updateTask/updateTask.fxml"));
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.DECORATED);

                UpdateTask updateController = fxmlLoader.getController();
                updateController.setUpdateTaskNameFld(tasks.getTask());
                updateController.setUpdateShortDescriptionFld(tasks.getDescription());
                updateController.setUpdateDatePicker(tasks.getDate());

                updateController.updateTaskBtn.setOnAction(event1 -> {
                    Calendar calendar = Calendar.getInstance();
                    Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
                    TodoTasks.getInstance().updateTask(updateController.getUpdateTaskNameFld(),timestamp,
                            updateController.getUpdateShortDescriptionFld(), updateController.getUpdateDatePicker(),
                            tasks.getTaskID());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Update Todo Item");
                    alert.setHeaderText("You have successfully updated a Task.");
                    alert.showAndWait();

                    updateController.updateTaskBtn.getScene().getWindow().hide();

                });
                stage.show();
            });


            deleteImg.setOnMouseClicked(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Todo Item");
                alert.setHeaderText("Delete item: " + tasks.getTask());
                alert.setContentText("Are you sure?  Press OK to confirm, or cancel to Back out.");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.isPresent() && (result.get() == ButtonType.OK)) {
                    Tasks item = getListView().getSelectionModel().getSelectedItem();
                    TodoTasks.getInstance().deleteTask(item, item.getTaskID());
                }

            });

            setText(null);
            setGraphic(cellRoot);
        }
    }
}