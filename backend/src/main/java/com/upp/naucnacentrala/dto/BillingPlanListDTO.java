package com.upp.naucnacentrala.dto;

import java.util.List;

public class BillingPlanListDTO {

    List<BillingPlanDTO> billingPlanDTOs;

    public List<BillingPlanDTO> getBillingPlanDTOs() {
        return billingPlanDTOs;
    }

    public void setBillingPlanDTOs(List<BillingPlanDTO> billingPlanDTOs) {
        this.billingPlanDTOs = billingPlanDTOs;
    }
}
