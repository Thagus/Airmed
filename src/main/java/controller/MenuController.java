package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MenuController {

    @FXML private StackPane stackPane;

    private HBox calendarView;
    private VBox patientsPane;
    private VBox treatmentsPane;
    private VBox studiesPane;
    private VBox configPane;

    private CalendarController calendarController;
    private PatientsController patientsController;
    private TreatmentsController treatmentsController;
    private StudiesController studiesController;
    private ConfigController configController;

    public void init() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/view/Calendar.fxml"));
        calendarView = loader.load();
        calendarController = loader.getController();
        calendarController.init();

        loader = new FXMLLoader(getClass().getResource("/view/Patients.fxml"));
        patientsPane = loader.load();
        patientsController = loader.getController();
        patientsController.init();

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

        stackPane.getChildren().addAll(
                calendarView,
                patientsPane,
                treatmentsPane,
                studiesPane,
                configPane
        );

        hideAll();
    }

    public void newPatient(ActionEvent actionEvent) {
    }

    public void newAppointment(ActionEvent actionEvent) {
    }

    public void newConsultation(ActionEvent actionEvent) {
    }

    public void newPrescription(ActionEvent actionEvent) {
    }

    public void showCalendar(ActionEvent actionEvent) {
        hideAll();
        calendarView.setVisible(true);
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
        calendarView.setVisible(false);
        patientsPane.setVisible(false);
        treatmentsPane.setVisible(false);
        studiesPane.setVisible(false);
        configPane.setVisible(false);
    }
}
