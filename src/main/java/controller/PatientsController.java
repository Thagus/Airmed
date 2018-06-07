package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.control.textfield.CustomTextField;

public class PatientsController {

    @FXML private CustomTextField searchField;
    @FXML private TableView patientsTable;

    @FXML private TableColumn nameColumn;
    @FXML private TableColumn lastnameColumn;
    @FXML private TableColumn gender;
    @FXML private TableColumn recordColumn;
    @FXML private TableColumn consultationColumn;
    @FXML private TableColumn deleteColumn;

    public void init() {

    }

    public void newPatient(ActionEvent actionEvent) {
    }
}
