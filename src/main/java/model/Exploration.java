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

    public static Exploration create(Consultation consultation, String awareness, String collaboration, String mobility, String attitude, String nutrition, String hydration){
        if(awareness.length()==0 && collaboration.length()==0 && mobility.length()==0 && attitude.length()==0 && nutrition.length()==0 && hydration.length()==0){
            return null;
        }

        Exploration exploration = new Exploration();

        exploration.id = consultation.getId();
        exploration.awareness = awareness;
        exploration.collaboration = collaboration;
        exploration.mobility = mobility;
        exploration.attitude = attitude;
        exploration.nutrition = nutrition;
        exploration.hydration = hydration;

        return exploration;
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
