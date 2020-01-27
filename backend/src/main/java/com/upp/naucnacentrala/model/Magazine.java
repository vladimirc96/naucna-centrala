package com.upp.naucnacentrala.model;


import com.upp.naucnacentrala.model.enums.BillingType;

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

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "is_registered")
    private boolean isRegistered = false;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "magazine_sciencefield",
            joinColumns = @JoinColumn(name = "magazine_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "sciencefield_id", referencedColumnName = "id"))
    private List<ScienceField> scienceFields;

    // recenzenti
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

    @OneToMany(mappedBy = "magazine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderObject> orderObjects;

    @OneToMany(mappedBy = "magazine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "magazine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SciencePaper> sciencePapers;

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
        this.scienceFieldEditors.clear();
        this.scienceFieldEditors.addAll(scienceFieldEditors);
    }

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

    public void addEditor(Editor editor){
        scienceFieldEditors.add(editor);
        editor.setMagazine(this);
    }

    public void removeEditor(Editor editor){
        editor.setMagazine(null);
        this.scienceFieldEditors.remove(editor);
    }

    public void clearEditors(){
        for(Editor editor: this.scienceFieldEditors){
            editor.setMagazine(null);
        }
        this.scienceFieldEditors.clear();
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public List<OrderObject> getOrderObjects() {
        return orderObjects;
    }

    public void setOrderObjects(List<OrderObject> orderObjects) {
        this.orderObjects = orderObjects;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<SciencePaper> getSciencePapers() {
        return sciencePapers;
    }

    public void setSciencePapers(List<SciencePaper> sciencePapers) {
        this.sciencePapers = sciencePapers;
    }
}
