package model;

import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Dose extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private String dose;

    @ManyToOne
    private Medicine medicine;

    @ManyToMany(mappedBy = "medicines")
    private List<Treatment> treatments;
}
