package model;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Prescription extends Model {
    @Id
    private int id;

    @ManyToOne
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    private String notes;

    @OneToOne(mappedBy = "prescription")
    private Consultation consultation;

    @ManyToMany
    private List<Treatment> treatments;

    @ManyToMany
    private List<Study> studies;

    @ManyToMany
    private List<Dose> medicines;

    public static Finder<Integer, Prescription> find = new Finder<>(Prescription.class);

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public List<Study> getStudies() {
        return studies;
    }

    public List<Dose> getMedicines() {
        return medicines;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }

    public void setStudies(List<Study> studies) {
        this.studies = studies;
    }

    public void setMedicines(List<Dose> medicines) {
        this.medicines = medicines;
    }
}
