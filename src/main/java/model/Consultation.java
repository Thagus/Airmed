package model;

import io.ebean.Model;

import javax.persistence.*;
import java.time.LocalDateTime;
import io.ebean.Finder;

@Entity
public class Consultation extends Model {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    private String diagnostic;

    private String prognosis;

    @OneToOne
    private Prescription prescription;

    @OneToOne(mappedBy = "consultation")
    private Measurement measurement;

    @OneToOne(mappedBy = "consultation")
    private VitalSign vitalSign;

    @OneToOne(mappedBy = "consultation")
    private Exploration exploration;
    
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

    public Exploration getExploration() {
        return exploration;
    }

    public void setExploration(Exploration exploration) {
        this.exploration = exploration;
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
}
