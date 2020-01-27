package com.upp.naucnacentrala.client;

import com.upp.naucnacentrala.dto.KPRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistrationClient {

    @Autowired
    RestTemplate restTemplate;

    private final String INIT_REG_ENDPOINT = "https://localhost:8500/sellers/sellers/register/init";

    public KPRegistrationDTO initRegistration(KPRegistrationDTO kprDTO) {

        ResponseEntity<KPRegistrationDTO> kpResponse = restTemplate.postForEntity(this.INIT_REG_ENDPOINT,
                new HttpEntity<>(kprDTO), KPRegistrationDTO.class);

        return kpResponse.getBody();
    }

}
