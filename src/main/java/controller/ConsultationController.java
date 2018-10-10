package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.*;
import org.apache.commons.lang3.math.NumberUtils;
import utils.TableFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class ConsultationController {

    @FXML private TextField pressureField;
    @FXML private TextField breathField;
    @FXML private TextField pulseField;
    @FXML private TextField temperatureField;

    @FXML private TextField heightField;
    @FXML private TextField weightField;

    @FXML private TextField awarenessField;
    @FXML private TextField collaborationField;
    @FXML private TextField mobilityField;
    @FXML private TextField attitudeField;
    @FXML private TextField nutritionField;
    @FXML private TextField hydrationField;

    @FXML private TextArea diagnosisArea;
    @FXML private TextArea prognosisArea;

    private Consultation consultation;
    private MenuController menuController;

    public void init(MenuController menuController){
        this.menuController = menuController;

        //Add filters to vital sign fields
        pressureField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || !newValue.matches("/")) {
                pressureField.setText(newValue.replaceAll("[^\\d/]", ""));
            }
        });
        breathField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                breathField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        pulseField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                pulseField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        temperatureField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                temperatureField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        //Add numeric only filters to measurement fields
        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                heightField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        weightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                weightField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        diagnosisArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));

        prognosisArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
    }

    public void setPatient(Patient patient) {
        //Find if the patient had an appointment that day
        LocalDate today = LocalDate.now();
        Appointment appointment = Appointment.find.query().where().eq("patient", patient).between("dateTime", LocalDateTime.of(today, LocalTime.MIN), LocalDateTime.of(today, LocalTime.MAX)).findOne();

        if(appointment!=null){
            appointment.setAnswered(true);
            appointment.update();
        }

        consultation = new Consultation(patient, LocalDateTime.now());
    }

    public void goToPrescription(ActionEvent actionEvent) {
        String[] pressures = pressureField.getText().split("/");

        int pressureD = 0;
        int pressureS = 0;

        if(pressures.length>=2) {
            pressureD = NumberUtils.toInt(pressures[0], 0);
            pressureS = NumberUtils.toInt(pressures[1], 0);
        }

        //Save data to consultation
        consultation.setVitalSign(VitalSign.create(
                consultation,
                pressureD,
                pressureS,
                NumberUtils.toInt(pulseField.getText(), 0),
                NumberUtils.toInt(temperatureField.getText(), 0),
                NumberUtils.toInt(breathField.getText(), 0)
        ));

        consultation.setMeasurement(Measurement.create(
                consultation,
                NumberUtils.toInt(weightField.getText(), 0),
                NumberUtils.toInt(heightField.getText(), 0)
        ));

        consultation.setExploration(Exploration.create(
                consultation,
                awarenessField.getText(),
                collaborationField.getText(),
                mobilityField.getText(),
                attitudeField.getText(),
                nutritionField.getText(),
                hydrationField.getText()
        ));

        consultation.setDiagnostic(diagnosisArea.getText());
        consultation.setPrognosis(prognosisArea.getText());

        //Begin the prescription stage
        menuController.beginPrescription(consultation);
    }

    public void newConsultation() {
        Dialog<Patient> patientDialog = new Dialog<>();
        patientDialog.setTitle("Nueva consulta");
        patientDialog.setHeaderText(null);

        //Add buttons
        ButtonType selectPatient = new ButtonType("Iniciar", ButtonBar.ButtonData.OK_DONE);
        patientDialog.getDialogPane().getButtonTypes().addAll(selectPatient, ButtonType.CLOSE);

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

        patientResult.ifPresent(menuController::beginConsultation);
    }
}
