package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;
import model.AutocompleteBindings;
import model.EmailManager;
import model.entities.Appointment;
import model.entities.Consultation;
import model.entities.Patient;
import utils.BMIUtils;
import utils.TableFactory;

import java.io.IOException;

public class MenuController {

    @FXML private StackPane stackPane;

    private HBox agendaView;
    private VBox patientsPane;
    private VBox treatmentsPane;
    private VBox studiesPane;
    private VBox settingsPane;
    private VBox patientRecordPane;
    private VBox prescriptionPane;
    private VBox consultationPane;

    private AgendaController agendaController;
    private PatientsController patientsController;
    private TreatmentsController treatmentsController;
    private StudiesController studiesController;
    private SettingsController settingsController;
    private RecordController recordController;
    private ConsultationController consultationController;
    private PrescriptionController prescriptionController;

    private Stage primaryStage;
    private JMetro jMetro;

    public void init(Stage primaryStage, JMetro jMetro) throws IOException {
        this.primaryStage = primaryStage;
        this.jMetro = jMetro;
        FXMLLoader loader;

        EmailManager.getInstance();
        AutocompleteBindings.getInstance();

        BMIUtils.init();
        TableFactory.init(this);

        loader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
        settingsPane = loader.load();
        settingsController = loader.getController();
        settingsController.init(settingsPane, this);

        loader = new FXMLLoader(getClass().getResource("/view/Agenda.fxml"));
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
        treatmentsController.init(this);

        loader = new FXMLLoader(getClass().getResource("/view/Studies.fxml"));
        studiesPane = loader.load();
        studiesController = loader.getController();
        studiesController.init(this);

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
        prescriptionController.init(this, prescriptionPane);

        stackPane.getChildren().addAll(
                agendaView,
                patientsPane,
                treatmentsPane,
                studiesPane,
                settingsPane,
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
        hideAll();
        recordController.setPatient(patient);
        patientRecordPane.setVisible(true);
    }

    public void newAppointment(ActionEvent actionEvent) {
        agendaController.newAppointment();
    }

    public void newConsultation(ActionEvent actionEvent) {
        consultationController.newConsultation();
    }

    public void startAppointment(Appointment appointment){
        hideAll();
        consultationController.startAppointment(appointment);
        consultationPane.setVisible(true);
    }

    public void beginConsultation(Patient patient){
        hideAll();
        consultationController.setPatient(patient);
        consultationPane.setVisible(true);
    }

    public void showConsultation(Consultation consultation) {
        hideAll();
        consultationController.showConsultation(consultation);
        consultationPane.setVisible(true);
    }

    public void beginPrescription(Consultation consultation){
        hideAll();
        prescriptionController.setConsultation(consultation);
        prescriptionPane.setVisible(true);
    }

    public void beginPrescription(Patient patient){
        hideAll();
        prescriptionController.setPatient(patient);
        prescriptionPane.setVisible(true);
    }

    public void showPrescription(Consultation consultation) {
        hideAll();
        prescriptionController.showPrescription(consultation);
        prescriptionPane.setVisible(true);
    }

    public void newPrescription(ActionEvent actionEvent) {
        prescriptionController.newPrescription();
    }

    public void showAgenda(ActionEvent actionEvent) {
        hideAll();
        agendaController.refresh();
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
        settingsPane.setVisible(true);
    }

    private void hideAll(){
        agendaView.setVisible(false);
        patientsPane.setVisible(false);
        treatmentsPane.setVisible(false);
        studiesPane.setVisible(false);
        settingsPane.setVisible(false);
        patientRecordPane.setVisible(false);
        consultationPane.setVisible(false);
        prescriptionPane.setVisible(false);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public JMetro getjMetro() {
        return jMetro;
    }
}
