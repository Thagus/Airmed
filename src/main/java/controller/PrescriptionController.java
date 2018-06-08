package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

    public void savePrescription(ActionEvent actionEvent) {
    }

    public void printPrescription(ActionEvent actionEvent) {
    }

    public void addMedicine(ActionEvent actionEvent) {
    }

    public void addStudy(ActionEvent actionEvent) {
    }

    public void addTreatment(ActionEvent actionEvent) {
    }
}
