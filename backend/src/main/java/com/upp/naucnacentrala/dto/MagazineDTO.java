package com.upp.naucnacentrala.dto;

import com.upp.naucnacentrala.model.Editor;
import com.upp.naucnacentrala.model.ScienceField;

import java.util.ArrayList;
import java.util.List;

public class MagazineDTO {

    private Long id;
    private String name;
    private String issn;
    private List<String> scienceFieldList = new ArrayList<>();
    private String chiefEditor;

    public MagazineDTO() {
    }

    public MagazineDTO(Long id, String name, String issn, List<ScienceField> scienceFieldList, Editor chiefEditor) {
        this.id = id;
        this.name = name;
        this.issn = issn;
        this.setScienceFieldList(scienceFieldList);
        this.setChiefEditor(chiefEditor);
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

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public List<String> getScienceFieldList() {
        return scienceFieldList;
    }

    public void setScienceFieldList(List<ScienceField> scienceFieldList) {
        for(ScienceField scienceField: scienceFieldList){
            this.scienceFieldList.add(scienceField.getName());
        }
    }
    public String getChiefEditor() {
        return chiefEditor;
    }
    public void setChiefEditor(Editor chiefEditor) {
        this.chiefEditor = chiefEditor.getFirstName() + " " + chiefEditor.getLastName();
    }
}
