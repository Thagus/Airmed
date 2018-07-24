package model;

import io.ebean.Model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Prescription extends Model {
    @Id
    private int id;

    @ManyToOne
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    private String diagnostic;

    private String prognosis;

    private String notes;

    @ManyToMany
    private List<Treatment> treatments;

    @ManyToMany
    private List<Study> studies;

    @OneToMany(mappedBy = "prescription")
    private List<PrescribedDose> medicines;
}
