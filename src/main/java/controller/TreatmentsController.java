package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.control.textfield.CustomTextField;

public class TreatmentsController {

    @FXML private TableView treatmentsTable;
    @FXML private CustomTextField searchField;

    @FXML private TableColumn descriptionColumn;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn editColumn;
    @FXML private TableColumn deleteColumn;

    public void init() {
    }

    public void newTreatment(ActionEvent actionEvent) {
    }
}
