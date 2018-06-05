package controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class TabController {

    @FXML private TabPane tabPane;

    @FXML private Tab calendarTab;
    @FXML private Tab patientsTab;
    @FXML private Tab treatmentsTab;
    @FXML private Tab studiesTab;
    @FXML private Tab configTab;


    @FXML private CalendarController calendarTabPageController;
    @FXML private PatientsController patientsTabPageController;
    @FXML private TreatmentsController treatmentsTabPageController;
    @FXML private StudiesController studiesTabPageController;
    @FXML private ConfigController configTabPageController;

    public void init() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab old, Tab newValue) -> {
            if(newValue == calendarTab){
                calendarTabPageController.handleBar();
            }
            else if(newValue == patientsTab){
                patientsTabPageController.handleBar();
            }
        });
    }
}
