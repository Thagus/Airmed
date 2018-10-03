package model;

import io.ebean.Finder;
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

    public static Finder<Integer, Dose> find = new Finder<>(Dose.class);

    public static Dose create(String dose, Medicine medicine) {
        Dose doseObj = new Dose();

        doseObj.dose = dose;
        doseObj.medicine = medicine;

        doseObj.save();
        return doseObj;
    }

    public int getId() {
        return id;
    }

    public String getDose() {
        return dose;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
}
