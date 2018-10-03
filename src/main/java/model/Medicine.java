package model;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Medicine extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "medicine")
    private List<Dose> doses;

    public static Finder<Integer, Medicine> find = new Finder<>(Medicine.class);

    public static Medicine create(String name, String description) {
        Medicine medicine = new Medicine();

        medicine.name = name;
        medicine.description = description;

        medicine.save();
        return medicine;
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

    public List<Dose> getDoses() {
        return doses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
