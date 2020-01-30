package com.upp.naucnacentrala.dto;

public class BillingPlanDTO {

    private long id;
    private String name;
    private String frequency;
    private String freqInterval;
    private String cycles;
    private double amount;
    private String currency;
    private double amountStart;

    public BillingPlanDTO() {}

    public BillingPlanDTO(long id, String name, String frequency, String freqInterval, String cycles, double amount, String currency, double amountStart) {
        this.id = id;
        this.name = name;
        this.frequency = frequency;
        this.freqInterval = freqInterval;
        this.cycles = cycles;
        this.amount = amount;
        this.currency = currency;
        this.amountStart = amountStart;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFreqInterval() {
        return freqInterval;
    }

    public void setFreqInterval(String freqInterval) {
        this.freqInterval = freqInterval;
    }

    public String getCycles() {
        return cycles;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmountStart() {
        return amountStart;
    }

    public void setAmountStart(double amountStart) {
        this.amountStart = amountStart;
    }
}
