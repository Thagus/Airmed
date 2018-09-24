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

    @OneToOne
    @JoinColumn(nullable = false)
    private Prescription prescription;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Measurement measurement;

    @OneToOne
    @PrimaryKeyJoinColumn
    private VitalSign vitalSign;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Exploration exploration;
    
    public static Finder<Integer, Consultation> find = new Finder<>(Consultation.class);

    public Consultation(Patient patient, LocalDateTime dateTime) {
        this.patient = patient;
        this.dateTime = dateTime;
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
}
