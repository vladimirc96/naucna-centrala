package com.upp.naucnacentrala.model;

import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;
import java.util.List;

@Entity
public class Magazine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "issn")
    private int issn;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sciencefield_magazine")
    private List<ScienceField> scienceFields;

    // uredjivacki odbor
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "magazine")
    private EditorialBoard editorialBoard;

    // recenzenti
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "magazine_reviewers")
    private List<Reviewer> reviewers;

    public Magazine() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ScienceField> getScienceFields() {
        return scienceFields;
    }

    public void setScienceFields(List<ScienceField> scienceFields) {
        this.scienceFields = scienceFields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIssn() {
        return issn;
    }

    public void setIssn(int issn) {
        this.issn = issn;
    }

    public EditorialBoard getEditorialBoard() {
        return editorialBoard;
    }

    public void setEditorialBoard(EditorialBoard editorialBoard) {
        this.editorialBoard = editorialBoard;
    }

    public List<Reviewer> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }
}
