package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;
import utils.ActionButtonTableCell;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    @FXML private TextField bloodTypeField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField cellphoneField;

    //Background
    @FXML private TextArea familyHistoryArea;
    @FXML private TextArea personalHistoryArea;
    @FXML private TextArea allergiesArea;
    @FXML private TextArea systemsArea;

    //Studies
    @FXML private CustomTextField studySearchField;
    @FXML private TableView<StudyResult> studiesTable;
    @FXML private TableColumn<StudyResult, String> studyNameColumn;
    @FXML private TableColumn<StudyResult, LocalDate> studyDateColumn;
    @FXML private TableColumn<StudyResult, String> studyDescColumn;
    @FXML private TableColumn<StudyResult, String> studyResultColumn;
    private ObservableList<StudyResult> studies;

    //Surgeries
    @FXML private CustomTextField surgerySearchField;
    @FXML private TableView<Surgery> surgeriesTable;
    @FXML private TableColumn<Surgery, String> surgeryNameColumn;
    @FXML private TableColumn<Surgery, LocalDate> surgeryDateColumn;
    @FXML private TableColumn<Surgery, String> surgeryDescColumn;
    private ObservableList<Surgery> surgeries;

    //Consultations
    @FXML private CustomTextField consultationSearchField;
    @FXML private TableView<Consultation> consultationsTable;
    @FXML private TableColumn<Consultation, LocalDate> consultationDateColumn;
    @FXML private TableColumn<Consultation, String> consultationDiagnosisColumn;
    @FXML private TableColumn<Consultation, String> consultationPrognosisColumn;
    @FXML private TableColumn<Consultation, Button> consultationViewColumn;
    private ObservableList<Consultation> consultations;

    //Notes
    @FXML private TextArea notesArea;

    private MenuController menuController;

    private Patient patient;
    private Record patientRecord;

    public void init(MenuController menuController) {
        this.menuController = menuController;

        //Initialize studies table
        studiesTable.setPlaceholder(new Label("Sin resultados"));
        studies = FXCollections.observableArrayList();

        studyNameColumn.setCellValueFactory(cellData -> {
            StudyResult studyResult = cellData.getValue();
            return new SimpleObjectProperty<>(studyResult.getStudy().getName());
        });
        studyDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        studyDescColumn.setCellValueFactory(cellData -> {
            StudyResult studyResult = cellData.getValue();
            return new SimpleObjectProperty<>(studyResult.getStudy().getDescription());
        });
        studyResultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

        FilteredList<StudyResult> filteredStudies = new FilteredList<>(studies, p -> true);   //Wrap the observable list into a filtered list

        studySearchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredStudies.setPredicate(study -> {
                //If filter text is empty, display all.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                //Simplify the search string by striping accents and making it lowercase
                String lcSearch = StringUtils.stripAccents(newValue).toLowerCase();

                //Return true if all the search terms are contained in the name
                boolean match = true;
                for(String term : lcSearch.split("\\s+")){
                    match = match && (
                            StringUtils.stripAccents(study.getStudy().getName()).toLowerCase().contains(term) ||
                                    StringUtils.stripAccents(study.getStudy().getDescription()).toLowerCase().contains(term) ||
                                    StringUtils.stripAccents(study.getResult()).toLowerCase().contains(term) ||
                                    study.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")).contains(term)
                    );
                }

                return match;
            });
        }));

        SortedList<StudyResult> sortedStudies = new SortedList<>(filteredStudies);        //Wrap the filtered list in a sorted list
        sortedStudies.comparatorProperty().bind(studiesTable.comparatorProperty());   //Bind the sorted list comparator to the table comparator
        studiesTable.setItems(sortedStudies);

        //Initialize surgeries table
        surgeriesTable.setPlaceholder(new Label("Sin resultados"));
        surgeries = FXCollections.observableArrayList();

        surgeryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surgeryDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        surgeryDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        FilteredList<Surgery> filteredSurgeries = new FilteredList<>(surgeries, p -> true);   //Wrap the observable list into a filtered list

        surgerySearchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredSurgeries.setPredicate(surgery -> {
                //If filter text is empty, display all.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                //Simplify the search string by striping accents and making it lowercase
                String lcSearch = StringUtils.stripAccents(newValue).toLowerCase();

                //Return true if all the search terms are contained in the name
                boolean match = true;
                for(String term : lcSearch.split("\\s+")){
                    match = match && (
                            StringUtils.stripAccents(surgery.getName()).toLowerCase().contains(term) ||
                                    StringUtils.stripAccents(surgery.getDescription()).toLowerCase().contains(term) ||
                                    surgery.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")).contains(term)
                    );
                }

                return match;
            });
        }));

        SortedList<Surgery> sortedSurgeries = new SortedList<>(filteredSurgeries);        //Wrap the filtered list in a sorted list
        sortedSurgeries.comparatorProperty().bind(surgeriesTable.comparatorProperty());   //Bind the sorted list comparator to the table comparator
        surgeriesTable.setItems(sortedSurgeries);

        //Initialize consultations table
        consultationsTable.setPlaceholder(new Label("Sin resultados"));
        consultations = FXCollections.observableArrayList();

        consultationDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        consultationDiagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        consultationPrognosisColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        consultationViewColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Ver", (Consultation consultation) -> {
            menuController.showConsultation(consultation);

            return consultation;
        }));
    }

    public void setPatient(Patient patient){
        //Set record label to patient full name
        recordLabel.setText("Expediente: " + patient.getFullName());

        if(this.patient!=patient) {
            //Reset view if the patient is different
            recordTabPane.getSelectionModel().selectFirst();
        }

        //Fix some weird bug that loses the toggle buttons in a group
        genderToggleGroup.getToggles().setAll(maleToggle, femaleToggle);
        maleToggle.setUserData('M');
        femaleToggle.setUserData('F');

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
        bloodTypeField.setText(patient.getBloodType());


        patientRecord = patient.getRecord();

        if(patientRecord==null){
            patientRecord = Record.create(patient);
        }

        allergiesArea.setText(patientRecord.getAllergies());
        familyHistoryArea.setText(patientRecord.getFamilyBg());
        notesArea.setText(patientRecord.getNotes());
        personalHistoryArea.setText(patientRecord.getPersonalBg());
        systemsArea.setText(patientRecord.getSystemsBg());

        //Fill tables data
        studies.setAll(StudyResult.find.query().where().eq("record", patientRecord).findList());
        surgeries.setAll(Surgery.find.query().where().eq("record", patientRecord).findList());
        consultations.setAll(Consultation.find.query().where().eq("patient", patient).findList());

        studiesTable.refresh();
        surgeriesTable.refresh();
        consultationsTable.refresh();
    }

    public void newStudy(ActionEvent actionEvent) {

    }

    public void newSurgery(ActionEvent actionEvent) {

    }

    public void newConsultation(ActionEvent actionEvent) {
        menuController.beginConsultation(patient);
    }

    public void newPrescription(ActionEvent actionEvent) {
        menuController.beginPrescription(patient);
    }

    public void savePatient(ActionEvent actionEvent) {
        patient.setName(nameField.getText());
        patient.setLastname(lastnameField.getText());
        patient.setBirthdate(birthdateField.getValue());
        patient.setBloodType(bloodTypeField.getText());
        patient.setEmail(emailField.getText());
        patient.setPhone(phoneField.getText());
        patient.setCellphone(cellphoneField.getText());
        patient.setGender((Character) genderToggleGroup.getSelectedToggle().getUserData());

        patientRecord.setAllergies(allergiesArea.getText());
        patientRecord.setFamilyBg(familyHistoryArea.getText());
        patientRecord.setNotes(notesArea.getText());
        patientRecord.setPersonalBg(personalHistoryArea.getText());
        patientRecord.setSystemsBg(systemsArea.getText());

        patient.update();
        patientRecord.update();
    }

    private boolean isInputValid(){

        return false;
    }
}
