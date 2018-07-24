package model;

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
}
