package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.service.MagazineService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.JavaAWTAccess;

import java.util.List;

@Service
public class UpdateMagazine implements JavaDelegate{

    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // update za casopis
        Long id = (Long) delegateExecution.getVariable("magazineId");
        Magazine magazine = magazineService.findOneById(id);
        List<FormSubmissionDto> magazineCorrectionData = (List<FormSubmissionDto>) delegateExecution.getVariable("magazineCorrectionData");
        magazine = magazineService.magazineCorrection(magazineCorrectionData, magazine);

        // update varijable za formu
        delegateExecution.setVariable("naziv", magazine.getName());
        delegateExecution.setVariable("issn", magazine.getIssn());

        if(magazine.getBillingType().name().equals("AUTHORS")){
            delegateExecution.setVariable("nacinNaplacivanja", "Autorima");
        }else{
            delegateExecution.setVariable("nacinNaplacivanja", "Citaocima");
        }
    }

}