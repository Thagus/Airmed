package model;

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
}
