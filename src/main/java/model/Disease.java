package model;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@UniqueConstraint(columnNames = "name")
public class Disease extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "disease")
    private List<DiseaseStatus> statuses;

    public static Finder<Integer, Disease> find = new Finder<>(Disease.class);

    public static Disease create(String name){
        Disease disease = new Disease();

        disease.name = name;

        disease.save();
        return disease;
    }

    public String getName() {
        return name;
    }
}
