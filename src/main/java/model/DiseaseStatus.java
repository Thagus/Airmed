package model;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class DiseaseStatus extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private boolean controlled;

    @ManyToOne
    private Disease disease;

    @ManyToOne
    private Consultation consultation;

    public static Finder<Integer, DiseaseStatus> find = new Finder<>(DiseaseStatus.class);

    public static DiseaseStatus create(Disease disease, boolean controlled){
        DiseaseStatus diseaseStatus = new DiseaseStatus();

        diseaseStatus.disease = disease;
        diseaseStatus.controlled = controlled;

        diseaseStatus.save();
        return diseaseStatus;
    }

    public boolean isControlled() {
        return controlled;
    }

    public Disease getDisease() {
        return disease;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setControlled(boolean controlled) {
        this.controlled = controlled;
    }
}
