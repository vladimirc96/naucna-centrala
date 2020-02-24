package com.upp.naucnacentrala.dto;

public class SearchSciencePaperDTO {

    private String id;
    private String title;
    private String currency;
    private double price;
    private String highlight;
    private boolean openAccess;

    public SearchSciencePaperDTO() {
    }

    public SearchSciencePaperDTO(String id, String title, String currency, double price, String highlight, boolean openAccess) {
        this.id = id;
        this.title = title;
        this.currency = currency;
        this.price = price;
        this.highlight = highlight;
        this.openAccess = openAccess;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public boolean isOpenAccess() {
        return openAccess;
    }

    public void setOpenAccess(boolean openAccess) {
        this.openAccess = openAccess;
    }
}
