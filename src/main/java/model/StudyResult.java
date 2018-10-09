package model;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class StudyResult extends Model {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Study study;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Record record;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "clob")
    private String result;

    public static Finder<Integer, StudyResult> find = new Finder<>(StudyResult.class);

    public static StudyResult create(Study study, Record recod, LocalDate date, String result){
        StudyResult studyResult = new StudyResult();

        studyResult.study = study;
        studyResult.record = recod;
        studyResult.date = date;
        studyResult.result = result;

        studyResult.save();
        return studyResult;
    }

    public int getId() {
        return id;
    }

    public Study getStudy() {
        return study;
    }

    public Record getRecord() {
        return record;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getResult() {
        return result;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
