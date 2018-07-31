package model;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Exploration extends Model {
    @Id
    private int id;

    private String awareness;
    private String collaboration;
    private String mobility;
    private String attitude;
    private String nutrition;
    private String hydration;
}
