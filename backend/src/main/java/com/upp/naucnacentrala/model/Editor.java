package com.upp.naucnacentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("EDITOR")
public class Editor extends User {

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private EditorialBoard editorialBoard;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EditorialBoard getEditorialBoard() {
        return editorialBoard;
    }

    public void setEditorialBoard(EditorialBoard editorialBoard) {
        this.editorialBoard = editorialBoard;
    }
}
