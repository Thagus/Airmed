package model;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Setting extends Model {
    @Id
    private String setting;

    @Column(nullable = false)
    private String value;
}
