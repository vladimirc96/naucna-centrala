package com.upp.naucnacentrala.model;


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
    private String issn;

    @Column(name = "billing_type")
    @Enumerated(EnumType.STRING)
    private BillingType billingType;

    @Column(name = "is_active")
    private boolean isActive = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "magazine_sciencefield",
            joinColumns = @JoinColumn(name = "magazine_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "sciencefield_id", referencedColumnName = "id"))
    private List<ScienceField> scienceFields;
//
//    // uredjivacki odbor
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "magazine")
//    private EditorialBoard editorialBoard;

    // recenzenti
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "magazine_reviewers",
            joinColumns = @JoinColumn(name = "magazines_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "reviewer_username", referencedColumnName = "username"))
    private List<Reviewer> reviewers;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor_id")
    private Editor chiefEditor;

    // urednik je angazovan samo za jedan casopis, zato one to many
    @OneToMany(mappedBy = "magazine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Editor> scienceFieldEditors;

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

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public Editor getChiefEditor() {
        return chiefEditor;
    }

    public void setChiefEditor(Editor chiefEditor) {
        this.chiefEditor = chiefEditor;
    }

    public List<Editor> getScienceFieldEditors() {
        return scienceFieldEditors;
    }

    public void setScienceFieldEditors(List<Editor> scienceFieldEditors) {
        this.scienceFieldEditors = scienceFieldEditors;
    }

//    public EditorialBoard getEditorialBoard() {
//        return editorialBoard;
//    }
//
//    public void setEditorialBoard(EditorialBoard editorialBoard) {
//        this.editorialBoard = editorialBoard;
//    }

    public List<Reviewer> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }

    public BillingType getBillingType() {
        return billingType;
    }

    public void setBillingType(BillingType billingType) {
        this.billingType = billingType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
