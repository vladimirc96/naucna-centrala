package com.upp.naucnacentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class SciencePaper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "key_term")
    private String keyTerm;

    @Column(name = "paper_abstract")
    private String paperAbstract;

    @ManyToOne
    private ScienceField scienceField;

    @ManyToOne
    private Magazine magazine;

    @OneToMany(mappedBy = "sciencePaper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderObject> orderObjects;

    public SciencePaper() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyTerm() {
        return keyTerm;
    }

    public void setKeyTerm(String keyTerm) {
        this.keyTerm = keyTerm;
    }

    public String getPaperAbstract() {
        return paperAbstract;
    }

    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    public ScienceField getScienceField() {
        return scienceField;
    }

    public void setScienceField(ScienceField scienceField) {
        this.scienceField = scienceField;
    }

    public List<OrderObject> getOrderObjects() {
        return orderObjects;
    }

    public void setOrderObjects(List<OrderObject> orderObjects) {
        this.orderObjects = orderObjects;
    }
}
