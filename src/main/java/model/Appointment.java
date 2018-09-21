package model;

import io.ebean.Model;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private boolean answered;
    
    public static Finder<Integer, Appointment> find = new Finder<>(Appointment.class);

    public static Appointment create(Patient patient, LocalDate date, LocalTime time) {
        Appointment appointment = new Appointment();
        
        appointment.patient = patient;
        appointment.date = date;
        appointment.time = time;
        
        appointment.save();
        return appointment;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
