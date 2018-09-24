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

    public static VitalSign create(Consultation consultation, int pressureD, int pressureS, int pulse, int temperature, int breath){
        if(pressureD==0 && pressureS==0 && pulse==0 && temperature==0 && breath==0){
            return null;
        }

        VitalSign vitalSign = new VitalSign();

        vitalSign.id = consultation.getId();
        vitalSign.pressureD = pressureD;
        vitalSign.pressureS = pressureS;
        vitalSign.pulse = pulse;
        vitalSign.temperature = temperature;
        vitalSign.breath = breath;

        return vitalSign;
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
