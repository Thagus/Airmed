package controller;

import javafx.event.ActionEvent;
import model.Consultation;
import model.Patient;

public class ConsultationController {

    private Patient patient;
    private Consultation consultation;
    private MenuController menuController;

    public void init(MenuController menuController){
        this.menuController = menuController;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;

        consultation = new Consultation(
                //Assign consultation data
        );
    }

    public void goToPrescription(ActionEvent actionEvent) {
        menuController.beginPrescription(consultation);
    }
}
