package model;

import io.ebean.Model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import io.ebean.Finder;

@Entity
public class Appointment extends Model {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private boolean answered;
    
    public static Finder<Integer, Appointment> find = new Finder<>(Appointment.class);

    public static Appointment create(Patient patient, LocalDateTime dateTime) {
        Appointment appointment = new Appointment();
        
        appointment.patient = patient;
        appointment.dateTime = dateTime;
        
        appointment.save();
        return appointment;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}
