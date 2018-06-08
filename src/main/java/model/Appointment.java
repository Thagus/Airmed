package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private String patient;
    private LocalDate date;
    private LocalTime time;

    public Appointment(String patient, LocalDate date, LocalTime time) {
        this.patient = patient;
        this.date = date;
        this.time = time;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
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
