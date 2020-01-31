package com.upp.naucnacentrala.dto;

public class SciencePaperDTO {

    private Long id;
    private String title;
    private String keyTerm;
    private String paperAbstract;
    private double price;
    private String currency;
    private MagazineInfoDTO magazine;

    public SciencePaperDTO() {}

    public SciencePaperDTO(Long id, String title, String keyTerm, String paperAbstract, double price, String currency, MagazineInfoDTO magazine) {
        this.id = id;
        this.title = title;
        this.keyTerm = keyTerm;
        this.paperAbstract = paperAbstract;
        this.price = price;
        this.currency = currency;
        this.magazine = magazine;
    }

    public MagazineInfoDTO getMagazine() {
        return magazine;
    }

    public void setMagazine(MagazineInfoDTO magazine) {
        this.magazine = magazine;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
