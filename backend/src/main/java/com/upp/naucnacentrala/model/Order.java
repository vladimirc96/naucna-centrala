package com.upp.naucnacentrala.model;

import com.upp.naucnacentrala.model.enums.OrderStatus;
import com.upp.naucnacentrala.model.enums.OrderType;

import javax.persistence.*;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "magazine_id")
    private Long magazineId;

    @Column(name = "science_paper_id")
    private Long sciencePaperId;

    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    public Order() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMagazineId() {
        return magazineId;
    }

    public void setMagazineId(Long magazineId) {
        this.magazineId = magazineId;
    }

    public Long getSciencePaperId() {
        return sciencePaperId;
    }

    public void setSciencePaperId(Long sciencePaperId) {
        this.sciencePaperId = sciencePaperId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
