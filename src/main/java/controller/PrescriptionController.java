package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Consultation;
import model.Patient;
import model.Prescription;

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
        setConsultation(new Consultation(
                //Give patient
        ));
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
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

}
