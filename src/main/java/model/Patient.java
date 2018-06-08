package model;

import java.time.LocalDate;

public class Patient {
    private String name;
    private String lastname;
    private char gender;
    private LocalDate birthdate;
    private String email;
    private String phone;
    private String cellphone;

    public Patient(String name, String lastname, char gender, LocalDate birthdate, String email, String phone, String cellphone) {
        this.name = name;
        this.lastname = lastname;
        this.gender = gender;
        this.birthdate = birthdate;
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
}
