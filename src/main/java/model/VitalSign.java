package model;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VitalSign extends Model {
    @Id
    private int id;

    private int pressureD;
    private int pressureS;
    private int pulse;
    private int temperature;
    private int breath;
}
