package com.upp.naucnacentrala.model;

import com.upp.naucnacentrala.model.enums.Enums;

import javax.persistence.*;

@Entity
public class OrderObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private Enums.OrderType orderType;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private Enums.OrderStatus orderStatus;

    @ManyToOne
    private Magazine magazine;

    @ManyToOne
    private SciencePaper sciencePaper;

    @ManyToOne
    private Subscription subscription;

    public OrderObject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Magazine getMagazine() {
        return magazine;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }

    public SciencePaper getSciencePaper() {
        return sciencePaper;
    }

    public void setSciencePaper(SciencePaper sciencePaper) {
        this.sciencePaper = sciencePaper;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Enums.OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(Enums.OrderType orderType) {
        this.orderType = orderType;
    }

    public Enums.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Enums.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
