package model;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Exploration extends Model {
    @Id
    private int id;

    private String awareness;
    private String collaboration;
    private String mobility;
    private String attitude;
    private String nutrition;
    private String hydration;

    public Exploration(Consultation consultation, String awareness, String collaboration, String mobility, String attitude, String nutrition, String hydration) {
        this.id = consultation.getId();
        this.awareness = awareness;
        this.collaboration = collaboration;
        this.mobility = mobility;
        this.attitude = attitude;
        this.nutrition = nutrition;
        this.hydration = hydration;
    }

    public int getId() {
        return id;
    }

    public String getAwareness() {
        return awareness;
    }

    public void setAwareness(String awareness) {
        this.awareness = awareness;
    }

    public String getCollaboration() {
        return collaboration;
    }

    public void setCollaboration(String collaboration) {
        this.collaboration = collaboration;
    }

    public String getMobility() {
        return mobility;
    }

    public void setMobility(String mobility) {
        this.mobility = mobility;
    }

    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getHydration() {
        return hydration;
    }

    public void setHydration(String hydration) {
        this.hydration = hydration;
    }
}
