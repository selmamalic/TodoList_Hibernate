package main.java.selmamalic.com.todo.updateTask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.time.LocalDate;

public class UpdateTask {

    @FXML
    public JFXButton updateTaskBtn;

    @FXML
    private JFXTextArea updateShortDescriptionFld;

    @FXML
    private JFXTextField updateTaskNameFld;

    @FXML
    private JFXDatePicker updateDatePicker;

    public String getUpdateShortDescriptionFld() {
        return updateShortDescriptionFld.getText();
    }

    public void setUpdateShortDescriptionFld(String updateShortDescriptionFld) {
        this.updateShortDescriptionFld.setText(updateShortDescriptionFld);
    }

    public String getUpdateTaskNameFld() {
        return updateTaskNameFld.getText();
    }

    public void setUpdateTaskNameFld(String updateTaskNameFld) {
        this.updateTaskNameFld.setText(updateTaskNameFld);
    }

    public LocalDate getUpdateDatePicker() {
        String date = this.updateDatePicker.getValue().toString();
        return LocalDate.parse(date);
    }

    public void setUpdateDatePicker(LocalDate updateDatePicker) {
        this.updateDatePicker.setValue(updateDatePicker);
    }
}
