package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Patient;
import org.controlsfx.control.textfield.CustomTextField;

public class RecordController {
    @FXML private Label recordLabel;
    @FXML private TabPane recordTabPane;

    //General data
    @FXML private TextField nameField;
    @FXML private TextField lastnameField;
    @FXML private ToggleGroup genderToggleGroup;
    @FXML private ToggleButton maleToggle;
    @FXML private ToggleButton femaleToggle;
    @FXML private DatePicker birthdateField;
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

    private Patient patient;

    public void init() {
    }

    public void setPatient(Patient patient){
        //Set record label to patient full name
        recordLabel.setText("Expediente: " + patient.getFullName());

        //Reset view
        recordTabPane.getSelectionModel().selectFirst();

        //Fix some weird bug that loses the toggle buttons in a group
        genderToggleGroup.getToggles().setAll(maleToggle, femaleToggle);

        this.patient = patient;

        ///Check if values aren't null before assigning

        nameField.setText(patient.getName());
        lastnameField.setText(patient.getLastname());
        if(patient.getGender() == 'F'){
            genderToggleGroup.selectToggle(femaleToggle);
        }
        else if (patient.getGender() == 'M'){
            genderToggleGroup.selectToggle(maleToggle);
        }
        birthdateField.setValue(patient.getBirthdate());
        ///Compute age based on birthdate
        //ageField.setText();
        emailField.setText(patient.getEmail());
        phoneField.setText(patient.getPhone());
        cellphoneField.setText(patient.getCellphone());
    }

    public void newStudy(ActionEvent actionEvent) {

    }

    public void newSurgery(ActionEvent actionEvent) {

    }

    public void newVitalSign(ActionEvent actionEvent) {

    }

    public void savePatient(ActionEvent actionEvent) {

    }

    private boolean isInputValid(){

        return false;
    }


    public void newConsultation(ActionEvent actionEvent) {
    }

    public void newPrescription(ActionEvent actionEvent) {
    }
}
