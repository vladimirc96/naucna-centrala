package com.upp.naucnacentrala.dto;

import com.upp.naucnacentrala.model.Subscription;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

public class SubscriptionDTO {

    private Long id;
    private Date startDate;
    private Date endDate;
    private String userId;
    private MagazineDTO magazine;

    public SubscriptionDTO() {
    }

    public static SubscriptionDTO formDto(Subscription s, MagazineDTO mDTO) {
        if (s == null) {
            return new SubscriptionDTO();
        } else {
            return new SubscriptionDTO(s.getId(), s.getStartDate(), s.getEndDate(), s.getUserId(), mDTO);
        }
    }

    public SubscriptionDTO(Long id, Date startDate, Date endDate, String userId, MagazineDTO magazine) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.magazine = magazine;
    }

    public MagazineDTO getMagazine() {
        return magazine;
    }

    public void setMagazine(MagazineDTO magazine) {
        this.magazine = magazine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
