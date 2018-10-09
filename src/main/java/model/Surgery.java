package model;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Surgery extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Record record;

    private LocalDate date;

    private String description;

    public static Finder<Integer, Surgery> find = new Finder<>(Surgery.class);

    public static Surgery create(String name, Record record, LocalDate date, String description){
        Surgery surgery = new Surgery();

        surgery.name = name;
        surgery.record = record;
        surgery.date = date;
        surgery.description = description;

        surgery.save();
        return surgery;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Record getRecord() {
        return record;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
