package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.controlsfx.control.textfield.TextFields;
import utils.ActionButtonTableCell;
import utils.AutocompleteBindings;
import utils.IMCUtils;
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
    @FXML private TextField interpretationField;

    @FXML private TextField glucoseField;
    @FXML private TextField hemoglobinField;
    @FXML private TextField cholesterolField;
    @FXML private TextField triglyceridesField;

    @FXML private TableView<DiseaseStatus> diseasesTable;
    @FXML private TableColumn<DiseaseStatus, String> diseaseColumn;
    @FXML private TableColumn<DiseaseStatus, Boolean> diseaseControlColumn;
    @FXML private TableColumn<DiseaseStatus, Button> deleteDiseaseColumn;
    private ObservableList<DiseaseStatus> diseases;

    @FXML private TextArea motiveArea;
    @FXML private TextArea explorationArea;

    @FXML private TextArea diagnosisArea;
    @FXML private TextArea prognosisArea;

    private Consultation consultation;
    private MenuController menuController;

    private boolean showConsultation;

    public void init(MenuController menuController){
        this.menuController = menuController;
        this.showConsultation = false;

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
        glucoseField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                glucoseField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        hemoglobinField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                hemoglobinField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        cholesterolField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cholesterolField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        triglyceridesField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                triglyceridesField.setText(newValue.replaceAll("[^\\d]", ""));
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
                    imcField.setText(IMCUtils.calculateIMC(weight.get(), height.get()).toPlainString());
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
                    imcField.setText(IMCUtils.calculateIMC(weight.get(), height.get()).toPlainString());
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

        imcField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(consultation!=null){
                interpretationField.setText(IMCUtils.interpretIMC(new BigDecimal(imcField.getText()), consultation.getPatient().getBirthdate(), consultation.getPatient().getGender()));
            }
        });

        //Use TAB to change between text areas

        explorationArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.TAB && !event.isShiftDown() && !event.isControlDown()) {
                event.consume();
                Node node = (Node) event.getSource();
                KeyEvent newEvent = new KeyEvent(event.getSource(), event.getTarget(), event.getEventType(), event.getCharacter(), event.getText(), event.getCode(), event.isShiftDown(), true, event.isAltDown(), event.isMetaDown());
                node.fireEvent(newEvent);
            }
        });

        motiveArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.TAB && !event.isShiftDown() && !event.isControlDown()) {
                event.consume();
                Node node = (Node) event.getSource();
                KeyEvent newEvent = new KeyEvent(event.getSource(), event.getTarget(), event.getEventType(), event.getCharacter(), event.getText(), event.getCode(), event.isShiftDown(), true, event.isAltDown(), event.isMetaDown());
                node.fireEvent(newEvent);
            }
        });

        diagnosisArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.TAB && !event.isShiftDown() && !event.isControlDown()) {
                event.consume();
                Node node = (Node) event.getSource();
                KeyEvent newEvent = new KeyEvent(event.getSource(), event.getTarget(), event.getEventType(), event.getCharacter(), event.getText(), event.getCode(), event.isShiftDown(), true, event.isAltDown(), event.isMetaDown());
                node.fireEvent(newEvent);
            }
        });

        prognosisArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.TAB && !event.isShiftDown() && !event.isControlDown()) {
                event.consume();
                Node node = (Node) event.getSource();
                KeyEvent newEvent = new KeyEvent(event.getSource(), event.getTarget(), event.getEventType(), event.getCharacter(), event.getText(), event.getCode(), event.isShiftDown(), true, event.isAltDown(), event.isMetaDown());
                node.fireEvent(newEvent);
            }
        });

        motiveArea.setWrapText(true);
        explorationArea.setWrapText(true);
        diagnosisArea.setWrapText(true);
        prognosisArea.setWrapText(true);

        diseases = FXCollections.observableArrayList();
        diseaseColumn.setCellValueFactory(cellData -> {
            DiseaseStatus diseaseStatus = cellData.getValue();
            return new SimpleObjectProperty<>(diseaseStatus.getDisease().getName());
        });
        diseaseControlColumn.setCellValueFactory(d -> new SimpleBooleanProperty(d.getValue().isControlled()));
        diseaseControlColumn.setCellFactory( tc -> new CheckBoxTableCell<>());
        deleteDiseaseColumn.setCellFactory(ActionButtonTableCell.forTableColumn("X", (DiseaseStatus diseaseStatus) -> {
            diseases.remove(diseaseStatus);
            return diseaseStatus;
        }));
        diseasesTable.setItems(diseases);

        diseasesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void setPatient(Patient patient) {
        this.showConsultation = false;
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
        this.showConsultation = false;
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
                NumberUtils.toInt(breathField.getText(), 0),
                NumberUtils.toInt(glucoseField.getText(), 0),
                NumberUtils.toInt(hemoglobinField.getText(), 0),
                NumberUtils.toInt(cholesterolField.getText(), 0),
                NumberUtils.toInt(triglyceridesField.getText(), 0)
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

        consultation.setExploration(explorationArea.getText());

        consultation.setMotive(motiveArea.getText());
        consultation.setDiagnostic(diagnosisArea.getText());
        consultation.setPrognosis(prognosisArea.getText());

        consultation.setDiseases(diseases);

        //Begin the prescription stage showing it or creating it
        if(!showConsultation) {
            menuController.beginPrescription(consultation);
        }
        else {
            menuController.showPrescription(consultation);
        }
    }

    public void showConsultation(Consultation consultation){
        this.showConsultation = true;
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
            glucoseField.setText(consultation.getVitalSign().getGlucose() + "");
            hemoglobinField.setText(consultation.getVitalSign().getHemoglobin() + "");
            cholesterolField.setText(consultation.getVitalSign().getCholesterol() + "");
            triglyceridesField.setText(consultation.getVitalSign().getTriglycerides() + "");
        }

        if(consultation.getMeasurement()!=null) {
            heightField.setText(BigDecimal.valueOf(consultation.getMeasurement().getHeight()).setScale(2, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100)).toPlainString());
            weightField.setText(BigDecimal.valueOf(consultation.getMeasurement().getWeight()).setScale(1, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(1000)).toPlainString());
        }

        explorationArea.setText(consultation.getExploration());
        motiveArea.setText(consultation.getMotive());
        diagnosisArea.setText(consultation.getDiagnostic());
        prognosisArea.setText(consultation.getPrognosis());

        diseases.setAll(consultation.getDiseaseStatuses());
        diseasesTable.refresh();
    }

    public void newConsultation() {
        Dialog<Patient> patientDialog = new Dialog<>();
        patientDialog.setTitle("Nueva consulta");
        patientDialog.setHeaderText(null);
        menuController.getjMetro().applyTheme(patientDialog.getDialogPane());

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

    private void clearFields(){
        pressureField.setText("");
        breathField.setText("");
        pulseField.setText("");
        temperatureField.setText("");

        heightField.setText("");
        weightField.setText("");

        glucoseField.setText("");
        hemoglobinField.setText("");
        cholesterolField.setText("");
        triglyceridesField.setText("");

        explorationArea.setText("");

        motiveArea.setText("");
        diagnosisArea.setText("");
        prognosisArea.setText("");

        diseases.clear();
    }

    public void newDisease(ActionEvent actionEvent) {
        Dialog<DiseaseStatus> dialog = new Dialog<>();
        dialog.setTitle("Agregar enfermedad");
        dialog.setHeaderText(null);
        menuController.getjMetro().applyTheme(dialog.getDialogPane());

        // Add buttons
        ButtonType addButton = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButton);

        //Form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Nombre de la enfermedad");

        CheckBox statusBox = new CheckBox();

        grid.add(new Label("Nombre"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Controlada"), 0, 1);
        grid.add(statusBox, 1, 1);

        TextFields.bindAutoCompletion(nameField, AutocompleteBindings.getInstance().getDiseaseNames());

        dialog.getDialogPane().setContent(grid);

        //Focus the name field whe starting the dialog
        Platform.runLater(nameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == addButton){

                ///Check that all fields are correct
                String name = nameField.getText();
                boolean status = statusBox.isSelected();

                if(name.length()==0){
                    return null;
                }

                //Try to find if the disease already exists
                Disease disease = Disease.find.query().where().eq("name", name).findOne();

                //If the disease doesn't exist, create it
                if(disease==null) {
                    disease = Disease.create(name);
                    AutocompleteBindings.getInstance().addDiseaseName(name);
                }

                return DiseaseStatus.createWithoutSave(
                        disease,
                        consultation,
                        status
                );
            }

            return null;
        });

        Optional<DiseaseStatus> result = dialog.showAndWait();

        result.ifPresent(diseaseStatus -> {
            diseases.add(diseaseStatus);
            diseasesTable.refresh();
        });
    }
}
