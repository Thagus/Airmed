package model;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@UniqueConstraint(columnNames = "name")
public class Study extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "studies")
    private List<Prescription> prescriptions;

    public static Finder<Integer, Study> find = new Finder<>(Study.class);

    public static Study create(String name, String description) {
        Study study = new Study();

        study.name = name;
        study.description = description;

        try {
            study.save();
            return study;
        }
        catch (Exception ignored){
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
