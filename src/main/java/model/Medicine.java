package model;

import io.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Medicine extends Model {
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "medicine")
    private List<Dose> doses;
}
