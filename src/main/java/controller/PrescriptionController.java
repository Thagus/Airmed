package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Consultation;
import model.Patient;
import model.Prescription;
import utils.TableFactory;
import java.util.Optional;

public class PrescriptionController {

    @FXML private TextArea diagnosisArea;
    @FXML private TextArea prognosisArea;
    @FXML private TextArea notesArea;

    @FXML private TextField treatmentField;
    @FXML private TableView treatmentsTable;
    @FXML private TableColumn treatmentNameColumn;
    @FXML private TableColumn deleteTreatmentColumn;

    @FXML private TextField studyField;
    @FXML private TableView studiesTable;
    @FXML private TableColumn studyNameColumn;
    @FXML private TableColumn deleteStudyColumn;

    @FXML private TextField medicineField;
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
        this.consultation.setPrescription(prescription);
    }

    public void savePrescription(ActionEvent actionEvent) {
        //Get data
        prescription.setDiagnostic(diagnosisArea.getText());
        prescription.setPrognosis(prognosisArea.getText());
        prescription.setNotes(notesArea.getText());

        ///Save to database
        if(consultation!=null){
            consultation.save();
        }
        else {
            prescription.save();
        }

        //Return to the agenda
        menuController.showAgenda(null);
    }

    public void printPrescription(ActionEvent actionEvent) {
        //Save to database
        savePrescription(null);

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
        Dialog<Patient> patientDialog = new Dialog<>();
        patientDialog.setTitle("Nueva receta");
        patientDialog.setHeaderText(null);

        //Add buttons
        ButtonType selectPatient = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        patientDialog.getDialogPane().getButtonTypes().addAll(selectPatient, ButtonType.CLOSE);

        //Show the patients list
        VBox vBox = new VBox();
        TableView<Patient> patientTableView = TableFactory.createPatientTable(vBox);
        patientDialog.getDialogPane().setContent(vBox);

        patientDialog.setResultConverter(patientDialogButton -> {
            if(patientDialogButton == selectPatient){
                //Return the selected patient or an alert if null
                return patientTableView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        Optional<Patient> patientResult = patientDialog.showAndWait();

        patientResult.ifPresent(menuController::beginPrescription);
    }
}
