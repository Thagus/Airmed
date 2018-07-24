package model;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PrescribedDose extends Model {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Medicine medicine;

    private String dose;
}
