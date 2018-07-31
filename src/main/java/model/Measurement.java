package model;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Measurement extends Model {
    @Id
    private int id;

    private int weight;
    private int height;
}
