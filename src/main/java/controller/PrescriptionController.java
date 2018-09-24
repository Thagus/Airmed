package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Consultation;
import model.Patient;
import model.Prescription;
import org.controlsfx.control.textfield.CustomTextField;

import java.time.LocalDate;
import java.util.Optional;

public class PrescriptionController {

    @FXML private TextArea diagnosisArea;
    @FXML private TextArea prognosisArea;
    @FXML private TextArea notesArea;

    @FXML private TextField treatmentField;
    @FXML private TextField medicineField;
    @FXML private TextField studyField;

    @FXML private TableView treatmentsTable;
    @FXML private TableColumn treatmentNameColumn;
    @FXML private TableColumn deleteTreatmentColumn;

    @FXML private TableView studiesTable;
    @FXML private TableColumn studyNameColumn;
    @FXML private TableColumn deleteStudyColumn;

    @FXML private TableView medicinesTable;
    @FXML private TableColumn medicineNameColumn;
    @FXML private TableColumn doseColumn;
    @FXML private TableColumn deleteMedicineColumn;

    private Consultation consultation;
    private Prescription prescription;

    private MenuController menuController;

    public void init(MenuController menuController){
        this.menuController = menuController;
    }

    public void setPatient(Patient patient) {
        this.consultation = null;
        this.prescription = new Prescription();
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
        this.prescription = new Prescription();
    }

    public void savePrescription(ActionEvent actionEvent) {
        ///Save to database

        //Return to the agenda
        menuController.showAgenda(null);
    }

    public void printPrescription(ActionEvent actionEvent) {
        //Save to database

        //Show printing dialog

        //Return to the agenda
        menuController.showAgenda(null);
    }

    public void addMedicine(ActionEvent actionEvent) {
    }

    public void addStudy(ActionEvent actionEvent) {
    }

    public void addTreatment(ActionEvent actionEvent) {
    }

    public void newPrescription() {
        Dialog patientDialog = new Dialog();
        patientDialog.setTitle("Nueva receta");
        patientDialog.setHeaderText(null);

        //Add buttons
        ButtonType selectPatient = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        patientDialog.getDialogPane().getButtonTypes().addAll(selectPatient, ButtonType.CLOSE);

        //Show the patients list
        VBox vBox = new VBox();

        CustomTextField searchField = new CustomTextField();
        Label searchLabel = new Label();
        searchLabel.setStyle("-fx-padding: 0 2 0 7;");
        FontAwesomeIconView searchIcon = new FontAwesomeIconView();
        searchIcon.setGlyphName("SEARCH");
        searchIcon.setGlyphSize(13);
        searchLabel.setGraphic(searchIcon);
        searchField.setLeft(searchLabel);
        searchField.setPromptText("Buscar");
        searchField.setStyle("-fx-border-radius: 50 50 50 50; -fx-background-radius: 50 50 50 50;");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Region(), searchField);

        TableView<Patient> patientTableView = new TableView<>();

        TableColumn<Patient, String> nameColumn = new TableColumn<>("Nombre");
        TableColumn<Patient, String> lastnameColumn = new TableColumn<>("Apellido");
        TableColumn<Patient, Character> genderColumn = new TableColumn<>("Género");
        TableColumn<Patient, Integer> ageColumn = new TableColumn<>("Edad");

        patientTableView.getColumns().addAll(nameColumn, lastnameColumn, genderColumn, ageColumn);

        vBox.getChildren().addAll(hBox, patientTableView);

        patientDialog.getDialogPane().setContent(vBox);

        patientDialog.setResultConverter(patientDialogButton -> {
            if(patientDialogButton == selectPatient){
                //Return the selected patient or an alert if null
                return Patient.create(
                        "Jane",
                        "Doe",
                        'F',
                        "0+",
                        LocalDate.now(),
                        "",
                        "",
                        ""
                );
            }
            return null;
        });

        Optional<Patient> patientResult = patientDialog.showAndWait();

        patientResult.ifPresent(menuController::beginPrescription);
    }
}
