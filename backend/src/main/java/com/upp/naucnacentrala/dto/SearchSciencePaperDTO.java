package com.upp.naucnacentrala.dto;

public class SearchSciencePaperDTO {

    private String id;
    private String title;

    public SearchSciencePaperDTO() {
    }

    public SearchSciencePaperDTO(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
