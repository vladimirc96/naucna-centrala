package com.upp.naucnacentrala.dto;

import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.OrderObject;
import com.upp.naucnacentrala.model.enums.Enums;

import javax.persistence.*;

public class OrderDTO {

    private Long id;
    private String userId;
    private double amount;
    private Enums.OrderType orderType;
    private Enums.OrderStatus orderStatus;
    private MagazineDTO magazine;
    private SciencePaperDTO sciencePaper;

    public OrderDTO() {
    }

    public OrderDTO(Long id, String userId, double amount, Enums.OrderType orderType, Enums.OrderStatus orderStatus, MagazineDTO magazine, SciencePaperDTO sciencePaper) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.magazine = magazine;
        this.sciencePaper = sciencePaper;
    }

    public static OrderDTO formDto(OrderObject o) {
        if (o == null) {
            return new OrderDTO();
        } else {
            OrderDTO oDTO = new OrderDTO();
            oDTO.setId(o.getId());
            oDTO.setUserId(o.getUserId());
            oDTO.setAmount(o.getAmount());
            oDTO.setOrderType(o.getOrderType());
            oDTO.setOrderStatus(o.getOrderStatus());

            if (o.getMagazine() != null
                    && (o.getOrderType() == Enums.OrderType.ORDER_CASOPIS || o.getOrderType() == Enums.OrderType.ORDER_SUBSCRIPTION)) {
                MagazineDTO mDTO = new MagazineDTO(o.getMagazine().getId(), o.getMagazine().getName(),
                        o.getMagazine().getIssn(),
                        o.getMagazine().getScienceFields(),
                        o.getMagazine().getChiefEditor(), o.getMagazine().isRegistered(), o.getMagazine().getSellerId(),
                        o.getMagazine().getSciencePapers());
                oDTO.setMagazine(mDTO);
            }

            if (o.getSciencePaper() != null && o.getOrderType() == Enums.OrderType.ORDER_RAD) {
                SciencePaperDTO spDTO = new SciencePaperDTO();
                spDTO.setId(o.getSciencePaper().getId());
                spDTO.setTitle(o.getSciencePaper().getTitle());
                oDTO.setSciencePaper(spDTO);
            }

            return oDTO;


        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public MagazineDTO getMagazine() {
        return magazine;
    }

    public void setMagazine(MagazineDTO magazine) {
        this.magazine = magazine;
    }

    public SciencePaperDTO getSciencePaper() {
        return sciencePaper;
    }

    public void setSciencePaper(SciencePaperDTO sciencePaper) {
        this.sciencePaper = sciencePaper;
    }
}
