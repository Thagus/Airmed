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
    private boolean status;

    @ManyToOne
    private Disease disease;

    @ManyToOne
    private Consultation consultation;

    public static Finder<Integer, DiseaseStatus> find = new Finder<>(DiseaseStatus.class);
}
