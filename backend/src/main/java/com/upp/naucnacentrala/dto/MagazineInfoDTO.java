package com.upp.naucnacentrala.dto;


public class MagazineInfoDTO {

    private Long id;
    private String name;
    private String issn;
    private String currency;
    private double amount;

    public MagazineInfoDTO() {
    }

    public MagazineInfoDTO(Long id, String name, String issn, String currency, double amount) {
        this.id = id;
        this.name = name;
        this.issn = issn;
        this.currency = currency;
        this.amount = amount;
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

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
