package model;

import io.ebean.Model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Patient extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private char gender;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false, length = 3)
    private String bloodType;

    private String email;

    private String phone;

    private String cellphone;

    @OneToOne(mappedBy = "patient")
    @JoinColumn(nullable = false)
    private Record record;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    public Patient(String name, String lastname, char gender, String bloodType, LocalDate birthdate, String email, String phone, String cellphone) {
        this.name = name;
        this.lastname = lastname;
        this.gender = gender;
        this.birthdate = birthdate;
        this.bloodType = bloodType;
        this.email = email;
        this.phone = phone;
        this.cellphone = cellphone;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public char getGender() {
        return gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public int getId() {
        return id;
    }

    public String getBloodType() {
        return bloodType;
    }

    public Record getRecord() {
        return record;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}
