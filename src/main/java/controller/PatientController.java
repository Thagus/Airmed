package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.CustomTextField;

public class PatientController {

    //General data
    public TextField nameField;
    public TextField lastnameField;
    public TextField genderField;
    public TextField birthdateField;
    public TextField ageField;
    public TextField bloodTypeField;
    public TextField emailField;
    public TextField addressField;
    public TextField phoneField;
    public TextField cellphoneField;

    //Background
    public TextArea familyHistoryArea;
    public TextArea personalHistoryArea;
    public TextArea allergiesArea;
    public TextArea systemsArea;

    //Studies
    public CustomTextField studySearchField;
    public TableView studiesTable;
    public TableColumn studyNameColumn;
    public TableColumn studyDateColumn;
    public TableColumn studyDescColumn;
    public TableColumn studyResultColumn;

    //Surgeries
    public CustomTextField surgerySearchField;
    public TableView surgeriesTable;
    public TableColumn surgeryNameColumn;
    public TableColumn surgeryDateColumn;
    public TableColumn surgeryDescColumn;

    //Vital signs
    public CustomTextField vitalSignsSearchField;
    public TableView vitalSignsTable;
    public TableColumn vitalSignDateColumn;
    public TableColumn pressureColumn;
    public TableColumn respirationColumn;
    public TableColumn temperatureColumn;
    public TableColumn glucoseColumn;

    //Notes
    public TextArea notesArea;

    public void newStudy(ActionEvent actionEvent) {
    }

    public void newSurgery(ActionEvent actionEvent) {
    }

    public void newVitalSign(ActionEvent actionEvent) {
    }
}
