package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.*;
import org.apache.commons.lang3.math.NumberUtils;
import utils.TableFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsultationController {
    @FXML private Label consultationLabel;

    @FXML private TextField pressureField;
    @FXML private TextField breathField;
    @FXML private TextField pulseField;
    @FXML private TextField temperatureField;

    @FXML private TextField heightField;
    @FXML private TextField weightField;
    @FXML private TextField imcField;

    @FXML private TextField motiveField;
    @FXML private TextArea explorationField;

    @FXML private TextArea diagnosisArea;
    @FXML private TextArea prognosisArea;

    private Consultation consultation;
    private MenuController menuController;

    private boolean onlyShowConsultation;

    public void init(MenuController menuController){
        this.menuController = menuController;
        this.onlyShowConsultation = false;

        //Add filters to vital sign fields
        pressureField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*/")) {
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

        //Values to keep track of imc
        AtomicInteger weight = new AtomicInteger(0);
        AtomicInteger height = new AtomicInteger(0);

        //Add numeric only filters to measurement fields
        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)")) {
                heightField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
            else {
                height.set(new BigDecimal(heightField.getText()).multiply(BigDecimal.valueOf(100)).intValue());

                if(weight.get()!=0){
                    imcField.setText(BigDecimal.valueOf((weight.get()/((double)height.get()*(double)height.get()))*10).setScale(2, RoundingMode.CEILING).toPlainString());
                }
            }
            if(newValue.length()==0){
                imcField.setText("");
                height.set(0);
            }
        });
        weightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)")) {
                weightField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
            else {
                weight.set(new BigDecimal(weightField.getText()).multiply(BigDecimal.valueOf(1000)).intValue());

                if(height.get()!=0){
                    imcField.setText(BigDecimal.valueOf((weight.get()/((double)height.get()*(double)height.get()))*10).setScale(2, RoundingMode.CEILING).toPlainString());
                }
                else {
                    imcField.setText("");
                }
            }
            if(newValue.length()==0){
                imcField.setText("");
                weight.set(0);
            }
        });

        imcField.setEditable(false);

        diagnosisArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));

        prognosisArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
    }

    public void setPatient(Patient patient) {
        this.onlyShowConsultation = false;
        changeFieldStatus(true);
        clearFields();

        consultationLabel.setText("Consulta de " + patient.getFullName());
        //Find if the patient had an appointment that day
        LocalDate today = LocalDate.now();
        Appointment appointment = Appointment.find.query().where().eq("patient", patient).between("dateTime", LocalDateTime.of(today, LocalTime.MIN), LocalDateTime.of(today, LocalTime.MAX)).findOne();

        if(appointment!=null){
            appointment.setAnswered(true);
            appointment.update();
        }

        consultation = Consultation.create(patient, LocalDateTime.now());
    }

    public void startAppointment(Appointment appointment){
        this.onlyShowConsultation = false;
        changeFieldStatus(true);
        clearFields();

        if(appointment!=null){
            appointment.setAnswered(true);
            appointment.update();

            consultation = Consultation.create(appointment.getPatient(), LocalDateTime.now());
        }
        else {
            menuController.showAgenda(null);
        }
    }

    public void goToPrescription(ActionEvent actionEvent) {
        if(!onlyShowConsultation) {
            String[] pressures = pressureField.getText().split("/");

            int pressureD = 0;
            int pressureS = 0;

            if (pressures.length >= 2) {
                pressureS = NumberUtils.toInt(pressures[0], 0);
                pressureD = NumberUtils.toInt(pressures[1], 0);
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

            int weight = 0;
            int height= 0;

            if(weightField.getText().length()>0){
                weight = new BigDecimal(weightField.getText()).multiply(BigDecimal.valueOf(1000)).intValue();
            }

            if(heightField.getText().length()>0){
                height = new BigDecimal(heightField.getText()).multiply(BigDecimal.valueOf(100)).intValue();
            }

            consultation.setMeasurement(Measurement.create(
                    consultation,
                    weight,
                    height
            ));

            consultation.setExploration(explorationField.getText());

            consultation.setMotive(motiveField.getText());
            consultation.setDiagnostic(diagnosisArea.getText());
            consultation.setPrognosis(prognosisArea.getText());

            //Begin the prescription stage
            menuController.beginPrescription(consultation);
        }
        else {
            menuController.showPrescription(consultation);
        }
    }

    public void showConsultation(Consultation consultation){
        this.onlyShowConsultation = true;
        this.consultation = consultation;
        clearFields();

        consultationLabel.setText("Consulta de " + consultation.getPatient().getFullName());

        //Fill textfields
        if(consultation.getVitalSign()!=null) {
            VitalSign vitalSign = consultation.getVitalSign();
            System.out.println(vitalSign);
            pressureField.setText(consultation.getVitalSign().getPressureS() + "/" + consultation.getVitalSign().getPressureD());
            breathField.setText(consultation.getVitalSign().getBreath() + "");
            pulseField.setText(consultation.getVitalSign().getPulse() + "");
            temperatureField.setText(consultation.getVitalSign().getTemperature() + "");
        }

        if(consultation.getMeasurement()!=null) {
            heightField.setText(BigDecimal.valueOf(consultation.getMeasurement().getHeight()).setScale(2, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100)).toPlainString());
            weightField.setText(BigDecimal.valueOf(consultation.getMeasurement().getWeight()).setScale(1, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(1000)).toPlainString());
        }

        explorationField.setText(consultation.getExploration());
        motiveField.setText(consultation.getMotive());
        diagnosisArea.setText(consultation.getDiagnostic());
        prognosisArea.setText(consultation.getPrognosis());

        //Disable input for fields
        changeFieldStatus(false);
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

    private void changeFieldStatus(boolean status){
        pressureField.setEditable(status);
        pressureField.setMouseTransparent(!status);
        pressureField.setFocusTraversable(status);
        breathField.setEditable(status);
        breathField.setMouseTransparent(!status);
        breathField.setFocusTraversable(status);
        pulseField.setEditable(status);
        pulseField.setMouseTransparent(!status);
        pulseField.setFocusTraversable(status);
        temperatureField.setEditable(status);
        temperatureField.setMouseTransparent(!status);
        temperatureField.setFocusTraversable(status);

        heightField.setEditable(status);
        heightField.setMouseTransparent(!status);
        heightField.setFocusTraversable(status);
        weightField.setEditable(status);
        weightField.setMouseTransparent(!status);
        weightField.setFocusTraversable(status);

        motiveField.setEditable(status);
        motiveField.setMouseTransparent(!status);
        motiveField.setFocusTraversable(status);

        explorationField.setEditable(status);
        explorationField.setMouseTransparent(!status);
        explorationField.setFocusTraversable(status);

        diagnosisArea.setEditable(status);
        diagnosisArea.setMouseTransparent(!status);
        diagnosisArea.setFocusTraversable(status);
        prognosisArea.setEditable(status);
        prognosisArea.setMouseTransparent(!status);
        prognosisArea.setFocusTraversable(status);
    }

    private void clearFields(){
        pressureField.setText("");
        breathField.setText("");
        pulseField.setText("");
        temperatureField.setText("");

        heightField.setText("");
        weightField.setText("");

        explorationField.setText("");

        motiveField.setText("");
        diagnosisArea.setText("");
        prognosisArea.setText("");
    }
}
