package com.upp.naucnacentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class EditorialBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "editor_id", nullable = false)
//    private Editor chiefEditor;
//
//    // urednik je angazovan samo za jedan casopis, zato one to many
//    @OneToMany(mappedBy = "editorialBoard", fetch = FetchType.LAZY)
//    private List<Editor> scienceFieldEditors;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "magazine_id", nullable = false)
//    private Magazine magazine;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Editor getChiefEditor() {
//        return chiefEditor;
//    }
//
//    public void setChiefEditor(Editor chiefEditor) {
//        this.chiefEditor = chiefEditor;
//    }
//
//    public List<Editor> getScienceFieldEditors() {
//        return scienceFieldEditors;
//    }
//
//    public void setScienceFieldEditors(List<Editor> scienceFieldEditors) {
//        this.scienceFieldEditors = scienceFieldEditors;
//    }
//
//    public Magazine getMagazine() {
//        return magazine;
//    }
//
//    public void setMagazine(Magazine magazine) {
//        this.magazine = magazine;
//    }
}
