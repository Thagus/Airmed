package model;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Measurement extends Model {
    @Id
    private int id;

    private int weight;
    private int height;

    public Measurement(Consultation consultation, int weight, int height) {
        this.id = consultation.getId();
        this.weight = weight;
        this.height = height;
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
