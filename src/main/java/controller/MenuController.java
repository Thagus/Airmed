package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Appointment;
import model.Consultation;
import model.Patient;
import model.Prescription;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.CustomTextField;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class MenuController {

    @FXML private StackPane stackPane;

    private HBox agendaView;
    private VBox patientsPane;
    private VBox treatmentsPane;
    private VBox studiesPane;
    private VBox configPane;
    private VBox patientRecordPane;
    private VBox prescriptionPane;
    private VBox consultationPane;

    private AgendaController agendaController;
    private PatientsController patientsController;
    private TreatmentsController treatmentsController;
    private StudiesController studiesController;
    private ConfigController configController;
    private RecordController recordController;
    private ConsultationController consultationController;
    private PrescriptionController prescriptionController;

    public void init() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/view/Agenda.fxml"));
        agendaView = loader.load();
        agendaController = loader.getController();
        agendaController.init(this);

        loader = new FXMLLoader(getClass().getResource("/view/Patients.fxml"));
        patientsPane = loader.load();
        patientsController = loader.getController();
        patientsController.init(this);

        loader = new FXMLLoader(getClass().getResource("/view/Treatments.fxml"));
        treatmentsPane = loader.load();
        treatmentsController = loader.getController();
        treatmentsController.init();

        loader = new FXMLLoader(getClass().getResource("/view/Studies.fxml"));
        studiesPane = loader.load();
        studiesController = loader.getController();
        studiesController.init();

        loader = new FXMLLoader(getClass().getResource("/view/Config.fxml"));
        configPane = loader.load();
        configController = loader.getController();
        configController.init();

        loader = new FXMLLoader(getClass().getResource("/view/Record.fxml"));
        patientRecordPane = loader.load();
        recordController = loader.getController();
        recordController.init(this);

        loader = new FXMLLoader(getClass().getResource("/view/Consultation.fxml"));
        consultationPane = loader.load();
        consultationController = loader.getController();
        consultationController.init(this);

        loader = new FXMLLoader(getClass().getResource("/view/Prescription.fxml"));
        prescriptionPane = loader.load();
        prescriptionController = loader.getController();
        prescriptionController.init(this);

        stackPane.getChildren().addAll(
                agendaView,
                patientsPane,
                treatmentsPane,
                studiesPane,
                configPane,
                patientRecordPane,
                consultationPane,
                prescriptionPane
        );

        hideAll();
    }

    public void newPatient(ActionEvent actionEvent) {
        patientsController.newPatient(null);
    }

    public void showPatientRecord(Patient patient){
        recordController.setPatient(patient);

        hideAll();
        patientRecordPane.setVisible(true);
    }

    public void newAppointment(ActionEvent actionEvent) {
        agendaController.newAppointment();
    }

    public void newConsultation(ActionEvent actionEvent) {
        consultationController.newConsultation();
    }

    public void startAppointment(Appointment appointment){
        consultationController.startAppointment(appointment);

        hideAll();
        consultationPane.setVisible(true);
    }

    public void beginConsultation(Patient patient){
        consultationController.setPatient(patient);

        hideAll();
        consultationPane.setVisible(true);
    }

    public void showConsultation(Consultation consultation) {
        consultationController.showConsultation(consultation);

        hideAll();
        consultationPane.setVisible(true);
    }

    public void beginPrescription(Consultation consultation){
        prescriptionController.setConsultation(consultation);

        hideAll();
        prescriptionPane.setVisible(true);
    }

    public void beginPrescription(Patient patient){
        prescriptionController.setPatient(patient);

        hideAll();
        prescriptionPane.setVisible(true);
    }

    public void showPrescription(Consultation consultation) {
        prescriptionController.showPrescription(consultation);

        hideAll();
        prescriptionPane.setVisible(true);
    }

    public void newPrescription(ActionEvent actionEvent) {
        prescriptionController.newPrescription();
    }

    public void showAgenda(ActionEvent actionEvent) {
        hideAll();
        agendaView.setVisible(true);
    }

    public void showPatients(ActionEvent actionEvent) {
        hideAll();
        patientsPane.setVisible(true);
    }

    public void showTreatments(ActionEvent actionEvent) {
        hideAll();
        treatmentsPane.setVisible(true);
    }

    public void showStudies(ActionEvent actionEvent) {
        hideAll();
        studiesPane.setVisible(true);
    }

    public void showConfig(ActionEvent actionEvent) {
        hideAll();
        configPane.setVisible(true);
    }

    private void hideAll(){
        agendaView.setVisible(false);
        patientsPane.setVisible(false);
        treatmentsPane.setVisible(false);
        studiesPane.setVisible(false);
        configPane.setVisible(false);
        patientRecordPane.setVisible(false);
        consultationPane.setVisible(false);
        prescriptionPane.setVisible(false);
    }
}
