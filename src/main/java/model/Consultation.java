package model;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Consultation extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime dateTime;

}
