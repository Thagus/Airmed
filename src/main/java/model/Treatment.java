package model;

import io.ebean.Finder;
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
    private List<Dose> medicines;

    @ManyToMany(mappedBy = "treatments")
    private List<Prescription> prescriptions;

    public static Finder<Integer, Treatment> find = new Finder<>(Treatment.class);

    public static Treatment create(String name, String description, List<Dose> medicines){
        Treatment treatment = new Treatment();

        treatment.name = name;
        treatment.description = description;
        treatment.medicines = medicines;

        treatment.save();
        return treatment;
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

    public List<Dose> getMedicines() {
        return medicines;
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

    public void setMedicines(List<Dose> medicines) {
        this.medicines = medicines;
    }
}
