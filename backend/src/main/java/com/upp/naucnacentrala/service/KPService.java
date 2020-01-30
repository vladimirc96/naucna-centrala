package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.client.RegistrationClient;
import com.upp.naucnacentrala.dto.*;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.repository.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import java.util.List;

@Service
public class KPService {

    private final String REG_STATUS_CALLBACK_URL = "https://localhost:8600/kp/registration/status";

    @Autowired
    MagazineRepository magazineRepository;

    @Autowired
    RegistrationClient registrationClient;

    @Autowired
    MagazineService magazineService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrderObjectService orderObjectService;


    public KPRegistrationDTO initRegistration(MagazineDTO magazineDTO) {

        Magazine m = magazineRepository.findOneById(magazineDTO.getId());

        // ako postoji seller id za tu instancu magazina, znaci da vec postoji registracija, uspesna ili neuspesna
        if (m.getSellerId() != null) {
            return reviewRegistration(m);
        }

        KPRegistrationDTO kprDTO = new KPRegistrationDTO();
        kprDTO.setRegistrationStatusCallbackUrl(this.REG_STATUS_CALLBACK_URL);

        kprDTO = registrationClient.initRegistration(kprDTO);

        m.setSellerId(kprDTO.getSellerId());

        magazineRepository.save(m);

        return kprDTO;
    }

    public void changeRegistrationStatus(KPRegistrationDTO kprDTO) {
        Magazine m = magazineRepository.findBySellerId(kprDTO.getSellerId()).get();
        // isStatus lmao fucking shit but who cares
        m.setRegistered(kprDTO.isStatus());
        magazineRepository.save(m);
        System.out.println("Magazine: " + m.getId() + " registration success: " + m.isRegistered());

    }

    public KPRegistrationDTO reviewRegistrationSeller(MagazineDTO magazineDTO) {
        Magazine m = magazineRepository.findOneById(magazineDTO.getId());
        return reviewRegistration(m);
    }

    private KPRegistrationDTO reviewRegistration(Magazine m) {
        KPRegistrationDTO kprDTO = new KPRegistrationDTO();
        kprDTO.setRegistrationStatusCallbackUrl(this.REG_STATUS_CALLBACK_URL);
        kprDTO.setSellerId(m.getSellerId());
        kprDTO = registrationClient.reviewRegistration(kprDTO);
        return kprDTO;
    }

    public StringDTO createPlan(long magId) {
        Magazine m = magazineService.findOneById(magId);
        List<SciencePaper> radovi = m.getSciencePapers();
        double amount = 0;
        for(SciencePaper rad : radovi) {
            amount += rad.getPrice();
        }
        amount = amount * 0.9;
        String currency = "USD";
        if(!radovi.isEmpty()) {
            currency = radovi.get(0).getCurrency();
        }
        double roundAmount = Math.round(amount * 100.0) / 100.0;
        MagazineInfoDTO magazineDTO = new MagazineInfoDTO(m.getName(), m.getIssn(), currency, roundAmount, m.getSellerId());
        ResponseEntity response = restTemplate.postForEntity("https://localhost:8500/sellers/sellers/createPlan", new HttpEntity<>(magazineDTO),
                String.class);
        StringDTO text = new StringDTO((String) response.getBody());
        return text;
    }

    public List<BillingPlanDTO> getMagazinePlans(long magId) {
        Magazine m = magazineService.findOneById(magId);
        ResponseEntity<List<BillingPlanDTO>> responseEntity = restTemplate.exchange("https://localhost:8500/paypal-service/paypal/getAllPlans/" + m.getSellerId(), HttpMethod.GET, null, new ParameterizedTypeReference<List<BillingPlanDTO>>(){});
        return responseEntity.getBody();
    }
}
