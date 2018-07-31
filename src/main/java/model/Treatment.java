package model;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Treatment extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToMany
    private List<Medicine> medicines;

    @ManyToMany(mappedBy = "treatments")
    private List<Prescription> prescriptions;
}
