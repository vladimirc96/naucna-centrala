package com.upp.naucnacentrala.client;

import com.upp.naucnacentrala.dto.AgreementListDTO;
import com.upp.naucnacentrala.dto.MagazineInfoDTO;
import com.upp.naucnacentrala.model.Magazine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KPClient {

    @Autowired
    RestTemplate restTemplate;

    private static final String BACKEND_URL = "https://localhost:192.168.43.124";

    public ResponseEntity createPlan(MagazineInfoDTO magazineDTO) {
        ResponseEntity response = restTemplate.postForEntity(this.BACKEND_URL + "/sellers/sellers/createPlan", new HttpEntity<>(magazineDTO),
                String.class);
        return response;
    }

    public ResponseEntity getMagazinePlans(Magazine m) {
        ResponseEntity response = restTemplate.getForEntity(this.BACKEND_URL + "/sellers/sellers/getPlans/" + m.getSellerId(),
                String.class);
        return response;
    }

    public ResponseEntity getUserAgreements(String korisnik) {
        ResponseEntity response = restTemplate.getForEntity(this.BACKEND_URL + "/paypal-service/paypal/getUserAgreements/" + korisnik,
                AgreementListDTO.class);
        return response;
    }

    public ResponseEntity cancelAgreement(long agrID) {
        ResponseEntity response = restTemplate.getForEntity(this.BACKEND_URL + "/paypal-service/paypal/cancelAgreement/" + agrID,
                String.class);
        return response;
    }
}
