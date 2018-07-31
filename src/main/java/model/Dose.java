package model;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Dose extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private String dose;

    @ManyToOne
    private Medicine medicine;
}
