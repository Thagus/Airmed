package model;

import io.ebean.Model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Consultation extends Model {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Record record;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @OneToOne
    @JoinColumn(nullable = false)
    private Prescription prescription;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Measurement measurement;

    @OneToOne
    @PrimaryKeyJoinColumn
    private VitalSign vitalSign;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Exploration exploration;
}
