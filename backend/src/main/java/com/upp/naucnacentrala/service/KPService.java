package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.client.RegistrationClient;
import com.upp.naucnacentrala.dto.KPRegistrationDTO;
import com.upp.naucnacentrala.dto.MagazineDTO;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.repository.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;

@Service
public class KPService {

    private final String REG_STATUS_CALLBACK_URL = "https://localhost:8600/kp/registration/status";

    @Autowired
    MagazineRepository magazineRepository;

    @Autowired
    RegistrationClient registrationClient;


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
}
