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

    public VitalSign(int pressureD, int pressureS, int pulse, int temperature, int breath) {
        this.pressureD = pressureD;
        this.pressureS = pressureS;
        this.pulse = pulse;
        this.temperature = temperature;
        this.breath = breath;
    }

    public int getId() {
        return id;
    }

    public int getPressureD() {
        return pressureD;
    }

    public void setPressureD(int pressureD) {
        this.pressureD = pressureD;
    }

    public int getPressureS() {
        return pressureS;
    }

    public void setPressureS(int pressureS) {
        this.pressureS = pressureS;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getBreath() {
        return breath;
    }

    public void setBreath(int breath) {
        this.breath = breath;
    }
}
