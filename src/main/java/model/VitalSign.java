package model;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
public class VitalSign extends Model {
    @Id
    private int id;

    @OneToOne
    private Consultation consultation;

    @DbDefault("0")
    private int pressureD;
    @DbDefault("0")
    private int pressureS;
    @DbDefault("0")
    private int pulse;
    @DbDefault("0")
    private int temperature;
    @DbDefault("0")
    private int breath;

    @DbDefault("0")
    private int glucose;
    @DbDefault("0")
    @Column(columnDefinition = "DECIMAL(5,2)")
    private BigDecimal hemoglobin;
    @DbDefault("0")
    private int cholesterol;
    @DbDefault("0")
    private int triglycerides;

    public static Finder<Integer, VitalSign> find = new Finder<>(VitalSign.class);

    public static VitalSign create(Consultation consultation, int pressureD, int pressureS, int pulse, int temperature, int breath, int glucose, BigDecimal hemoglobin, int cholesterol, int triglycerides){
        if(pressureD==0 && pressureS==0 && pulse==0 && temperature==0 && breath==0){
            return null;
        }

        VitalSign vitalSign = new VitalSign();

        vitalSign.consultation = consultation;
        vitalSign.pressureD = pressureD;
        vitalSign.pressureS = pressureS;
        vitalSign.pulse = pulse;
        vitalSign.temperature = temperature;
        vitalSign.breath = breath;

        vitalSign.glucose = glucose;
        vitalSign.hemoglobin = hemoglobin;
        vitalSign.cholesterol = cholesterol;
        vitalSign.triglycerides = triglycerides;

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

    public int getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
    }

    public BigDecimal getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(BigDecimal hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    public int getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(int cholesterol) {
        this.cholesterol = cholesterol;
    }

    public int getTriglycerides() {
        return triglycerides;
    }

    public void setTriglycerides(int triglycerides) {
        this.triglycerides = triglycerides;
    }
}
