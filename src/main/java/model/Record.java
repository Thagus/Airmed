package model;

import io.ebean.Model;

import javax.persistence.*;
import java.util.List;
import io.ebean.Finder;

@Entity
public class Record extends Model {
    @Id
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Patient patient;

    @Column(columnDefinition = "clob")
    private String familyBg;

    @Column(columnDefinition = "clob")
    private String personalBg;

    @Column(columnDefinition = "clob")
    private String systemsBg;

    @Column(columnDefinition = "clob")
    private String allergies;

    @Column(columnDefinition = "clob")
    private String notes;

    @OneToMany(mappedBy = "record")
    private List<StudyResult> studyResults;

    @OneToMany(mappedBy = "record")
    private List<Surgery> surgeries;
    
    public static Finder<Integer, Record> find = new Finder<>(Record.class);

    public static Record create(Patient patient){
        Record record = new Record();

        record.patient = patient;
        patient.setRecord(record);

        record.save();
        return record;
    }

    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getFamilyBg() {
        return familyBg;
    }

    public String getPersonalBg() {
        return personalBg;
    }

    public String getSystemsBg() {
        return systemsBg;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getNotes() {
        return notes;
    }

    public List<StudyResult> getStudyResults() {
        return studyResults;
    }

    public List<Surgery> getSurgeries() {
        return surgeries;
    }

    public void setFamilyBg(String familyBg) {
        this.familyBg = familyBg;
    }

    public void setPersonalBg(String personalBg) {
        this.personalBg = personalBg;
    }

    public void setSystemsBg(String systemsBg) {
        this.systemsBg = systemsBg;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
