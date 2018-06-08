package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.control.textfield.CustomTextField;

public class StudiesController {

    @FXML private CustomTextField searchField;

    @FXML private TableView studiesTable;

    @FXML private TableColumn nameColumn;
    @FXML private TableColumn descriptionColumn;
    @FXML private TableColumn editColumn;
    @FXML private TableColumn deleteColumn;

    public void init() {
    }

    public void newStudy(ActionEvent actionEvent) {
    }
}
