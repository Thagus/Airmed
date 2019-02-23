package model;

import controller.MenuController;
import io.ebean.Model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.ebean.Finder;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

@Entity
public class Consultation extends Model {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(columnDefinition = "clob")
    private String diagnostic;

    @Column(columnDefinition = "clob")
    private String prognosis;

    @Column(columnDefinition = "clob")
    private String motive;

    @Column(columnDefinition = "clob")
    private String exploration;

    @OneToOne
    private Prescription prescription;

    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL)
    private Measurement measurement;

    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL)
    private VitalSign vitalSign;

    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL)
    private List<DiseaseStatus> diseases;
    
    public static Finder<Integer, Consultation> find = new Finder<>(Consultation.class);

    public static Consultation create(Patient patient, LocalDateTime dateTime) {
        Consultation consultation = new Consultation();

        consultation.patient = patient;
        consultation.dateTime = dateTime;

        return consultation;
    }

    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public VitalSign getVitalSign() {
        return vitalSign;
    }

    public void setVitalSign(VitalSign vitalSign) {
        this.vitalSign = vitalSign;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getPrognosis() {
        return prognosis;
    }

    public void setPrognosis(String prognosis) {
        this.prognosis = prognosis;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public String getExploration() {
        return exploration;
    }

    public void setExploration(String exploration) {
        this.exploration = exploration;
    }

    public List<DiseaseStatus> getDiseaseStatuses() {
        return diseases;
    }

    public void setDiseases(List<DiseaseStatus> diseases) {
        this.diseases = diseases;
    }

    public void print(MenuController menuController) {
        VBox consultationPane = new VBox();

        GridPane patientDataGrid = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(34);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33);
        patientDataGrid.getColumnConstraints().addAll(col1, col2, col3);

        //General data
        TextFlow name = new TextFlow();
        Text nameTitle = new Text("Nombre: ");
        nameTitle.setStyle("-fx-font-weight: bold");
        Text nameText = new Text(patient.getFullName());
        name.getChildren().addAll(nameTitle, nameText);
        patientDataGrid.add(name, 0, 0);

        TextFlow date = new TextFlow();
        Text dateTitle = new Text("Fecha: ");
        dateTitle.setStyle("-fx-font-weight: bold");
        Text dateText = new Text(dateTime.format(DateTimeFormatter.ISO_DATE));
        date.getChildren().addAll(dateTitle, dateText);
        patientDataGrid.add(date, 0, 1);

        TextFlow age = new TextFlow();
        Text ageTitle = new Text("Edad: ");
        ageTitle.setStyle("-fx-font-weight: bold");
        Text ageText = new Text(patient.getAge() + "");
        age.getChildren().addAll(ageTitle, ageText);
        patientDataGrid.add(age, 0, 2);

        TextFlow gender = new TextFlow();
        Text genderTitle = new Text("Género: ");
        genderTitle.setStyle("-fx-font-weight: bold");
        Text genderText = new Text(patient.getGender() + "");
        gender.getChildren().addAll(genderTitle, genderText);
        patientDataGrid.add(gender, 0, 3);

        TextFlow blood = new TextFlow();
        Text bloodTitle = new Text("Tipo sangre: ");
        bloodTitle.setStyle("-fx-font-weight: bold");
        Text bloodText = new Text(patient.getBloodType());
        blood.getChildren().addAll(bloodTitle, bloodText);
        patientDataGrid.add(blood, 0, 4);


        TextFlow height = new TextFlow();
        Text heightTitle = new Text("Estatura: ");
        heightTitle.setStyle("-fx-font-weight: bold");
        height.getChildren().add(heightTitle);
        if(measurement!=null) {
            Text heightText = new Text(BigDecimal.valueOf(measurement.getHeight()).setScale(2, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100)).toPlainString() + " m");
            height.getChildren().add(heightText);
        }
        patientDataGrid.add(height, 1, 0);

        TextFlow width = new TextFlow();
        Text widthTitle = new Text("Peso: ");
        widthTitle.setStyle("-fx-font-weight: bold");
        width.getChildren().add(widthTitle);
        if(measurement!=null) {
            Text widthText = new Text(BigDecimal.valueOf(measurement.getWeight()).setScale(1, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(1000)).toPlainString() + " kg");
            width.getChildren().add(widthText);
        }
        patientDataGrid.add(width, 1, 1);

        TextFlow temperature = new TextFlow();
        Text temperatureTitle = new Text("Temperatura: ");
        temperatureTitle.setStyle("-fx-font-weight: bold");
        temperature.getChildren().add(temperatureTitle);
        if(vitalSign!=null) {
            Text temperatureText = new Text(vitalSign.getTemperature() + "°C");
            temperature.getChildren().add(temperatureText);
        }
        patientDataGrid.add(temperature, 1, 2);

        TextFlow pressure = new TextFlow();
        Text pressureTitle = new Text("Presión: ");
        pressureTitle.setStyle("-fx-font-weight: bold");
        pressure.getChildren().add(pressureTitle);
        if(vitalSign!=null) {
            Text pressureText = new Text(vitalSign.getPressureS() + "/" + vitalSign.getPressureD());
            pressure.getChildren().add(pressureText);
        }
        patientDataGrid.add(pressure, 1, 3);

        TextFlow pulse = new TextFlow();
        Text pulseTitle = new Text("Pulso: ");
        pulseTitle.setStyle("-fx-font-weight: bold");
        pulse.getChildren().add(pulseTitle);
        if(vitalSign!=null) {
            Text pulseText = new Text(vitalSign.getPulse() + "/min");
            pulse.getChildren().add(pulseText);
        }
        patientDataGrid.add(pulse, 1, 4);

        TextFlow glucose = new TextFlow();
        Text glucoseTitle = new Text("Glucosa: ");
        glucoseTitle.setStyle("-fx-font-weight: bold");
        glucose.getChildren().add(glucoseTitle);
        if(vitalSign!=null) {
            Text glucoseText = new Text(vitalSign.getGlucose() + " mg");
            glucose.getChildren().add(glucoseText);
        }
        patientDataGrid.add(glucose, 2, 0);

        TextFlow breath = new TextFlow();
        Text breathTitle = new Text("Respiración: ");
        breathTitle.setStyle("-fx-font-weight: bold");
        breath.getChildren().add(breathTitle);
        if(vitalSign!=null) {
            Text breathText = new Text(vitalSign.getBreath() + "/min");
            breath.getChildren().add(breathText);
        }
        patientDataGrid.add(breath, 2, 1);

        TextFlow hemoglobin = new TextFlow();
        Text hemoglobinTitle = new Text("Hemoglobina: ");
        hemoglobinTitle.setStyle("-fx-font-weight: bold");
        hemoglobin.getChildren().add(hemoglobinTitle);
        if(vitalSign!=null) {
            Text hemoglobinText = new Text(vitalSign.getHemoglobin() + "%");
            hemoglobin.getChildren().add(hemoglobinText);
        }
        patientDataGrid.add(hemoglobin, 2, 2);

        TextFlow cholesterol = new TextFlow();
        Text cholesterolTitle = new Text("Colesterol: ");
        cholesterolTitle.setStyle("-fx-font-weight: bold");
        cholesterol.getChildren().add(cholesterolTitle);
        if(vitalSign!=null) {
            Text cholesterolText = new Text(vitalSign.getCholesterol() + "%");
            cholesterol.getChildren().add(cholesterolText);
        }
        patientDataGrid.add(cholesterol, 2, 3);

        TextFlow triglycerides = new TextFlow();
        Text triglyceridesTitle = new Text("Triglicéridos: ");
        triglyceridesTitle.setStyle("-fx-font-weight: bold");
        triglycerides.getChildren().add(triglyceridesTitle);
        if(vitalSign!=null) {
            Text triglyceridesText = new Text(vitalSign.getTriglycerides() + "%");
            triglycerides.getChildren().add(triglyceridesText);
        }
        patientDataGrid.add(triglycerides, 2, 4);

        consultationPane.getChildren().add(patientDataGrid);

        //Motive, exploration, diagnostic and prognostic
        Label motiveHeader = new Label("\nMotivo de la consulta:");
        motiveHeader.setStyle("-fx-font-weight: bold");
        Text motiveText = new Text(motive);
        motiveText.wrappingWidthProperty().bind(consultationPane.prefWidthProperty());
        consultationPane.getChildren().addAll(motiveHeader, motiveText);

        Label explorationHeader = new Label("\nExploración:");
        explorationHeader.setStyle("-fx-font-weight: bold");
        Text explorationText = new Text(exploration);
        explorationText.wrappingWidthProperty().bind(consultationPane.prefWidthProperty());
        consultationPane.getChildren().addAll(explorationHeader, explorationText);

        Label diagnosticHeader = new Label("\nDiagnóstico:");
        diagnosticHeader.setStyle("-fx-font-weight: bold");
        Text diagnosticText = new Text(diagnostic);
        diagnosticText.wrappingWidthProperty().bind(consultationPane.prefWidthProperty());
        consultationPane.getChildren().addAll(diagnosticHeader, diagnosticText);

        Label prognosticHeader = new Label("\nPronóstico:");
        prognosticHeader.setStyle("-fx-font-weight: bold");
        Text prognosisText = new Text(prognosis);
        prognosisText.wrappingWidthProperty().bind(consultationPane.prefWidthProperty());
        consultationPane.getChildren().addAll(prognosticHeader, prognosisText);

        List<Dose> doses = new ArrayList<>(prescription.getMedicines());
        for(Treatment treatment : prescription.getTreatments()){
            doses.addAll(treatment.getMedicines());
        }

        //Add prescription to document
        if(doses.size()>0 || prescription.getStudies().size()>0 || (prescription.getNotes()!=null && prescription.getNotes().length()>0)) {
            Label prescriptionHeader = new Label("\nReceta otorgada:");
            prescriptionHeader.setStyle("-fx-font-weight: bold");
            consultationPane.getChildren().add(prescriptionHeader);
        }

        for(Dose dose : doses) {
            Text label = new Text(dose.getMedicine().getName() + "\n- " + dose.getDose());
            label.wrappingWidthProperty().bind(consultationPane.prefWidthProperty());
            consultationPane.getChildren().add(label);
        }

        for(Study study : prescription.getStudies()){
            Text label = new Text(study.getName() + "\n" + study.getDescription());
            label.wrappingWidthProperty().bind(consultationPane.prefWidthProperty());
            consultationPane.getChildren().add(label);
        }

        //Add notes if they exist
        if(prescription.getNotes()!=null && prescription.getNotes().length()>0) {
            Text label = new Text("\n" + prescription.getNotes());
            label.wrappingWidthProperty().bind(consultationPane.prefWidthProperty());
            consultationPane.getChildren().add(label);
        }

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(menuController.getPrimaryStage().getOwner())){
            consultationPane.setPrefWidth(printerJob.getJobSettings().getPageLayout().getPrintableWidth());
            boolean success = printerJob.printPage(consultationPane);
            if (success) {
                printerJob.endJob();
            }
        }
    }
}
