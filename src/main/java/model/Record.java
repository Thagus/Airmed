package model;

import io.ebean.Model;

import javax.persistence.*;
import java.util.List;
import io.ebean.Finder;

@Entity
public class Record extends Model {
    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "id")
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
}