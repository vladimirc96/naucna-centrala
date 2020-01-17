package com.upp.naucnacentrala.model;

import org.hibernate.annotations.Generated;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
public class ScienceField implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "scienceFields", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Magazine> magazines;

    @ManyToMany(mappedBy = "scienceFields", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users;

    public ScienceField() {
    }

    public ScienceField(String name){
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Magazine> getMagazines() {
        return magazines;
    }

    public void setMagazines(List<Magazine> magazines) {
        this.magazines = magazines;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
