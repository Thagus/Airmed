package model;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Measurement extends Model {
    @Id
    private int id;

    @OneToOne
    private Consultation consultation;

    private int weight;
    private int height;

    public static Measurement create(Consultation consultation, int weight, int height){
        if(weight==0 && height==0){
            return null;
        }

        Measurement measurement = new Measurement();

        measurement.consultation = consultation;
        measurement.weight = weight;
        measurement.height = height;

        return measurement;
    }

    public int getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
