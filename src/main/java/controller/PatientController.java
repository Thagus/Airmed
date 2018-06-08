package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.CustomTextField;

public class PatientController {

    //General data
    @FXML private TextField nameField;
    @FXML private TextField lastnameField;
    @FXML private TextField genderField;
    @FXML private TextField birthdateField;
    @FXML private TextField ageField;
    @FXML private TextField bloodTypeField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField cellphoneField;

    //Background
    @FXML private TextArea familyHistoryArea;
    @FXML private TextArea personalHistoryArea;
    @FXML private TextArea allergiesArea;
    @FXML private TextArea systemsArea;

    //Studies
    @FXML private CustomTextField studySearchField;
    @FXML private TableView studiesTable;
    @FXML private TableColumn studyNameColumn;
    @FXML private TableColumn studyDateColumn;
    @FXML private TableColumn studyDescColumn;
    @FXML private TableColumn studyResultColumn;

    //Surgeries
    @FXML private CustomTextField surgerySearchField;
    @FXML private TableView surgeriesTable;
    @FXML private TableColumn surgeryNameColumn;
    @FXML private TableColumn surgeryDateColumn;
    @FXML private TableColumn surgeryDescColumn;

    //Vital signs
    @FXML private CustomTextField vitalSignsSearchField;
    @FXML private TableView vitalSignsTable;
    @FXML private TableColumn vitalSignDateColumn;
    @FXML private TableColumn pressureColumn;
    @FXML private TableColumn respirationColumn;
    @FXML private TableColumn temperatureColumn;
    @FXML private TableColumn glucoseColumn;

    //Notes
    @FXML private TextArea notesArea;

    public void newStudy(ActionEvent actionEvent) {
    }

    public void newSurgery(ActionEvent actionEvent) {
    }

    public void newVitalSign(ActionEvent actionEvent) {
    }
}
