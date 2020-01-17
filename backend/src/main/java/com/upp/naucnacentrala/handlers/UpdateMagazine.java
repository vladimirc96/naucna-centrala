package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Editor;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.Reviewer;
import com.upp.naucnacentrala.model.ScienceField;
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
        String oldScienceFields = "";
        String oldEditors = "";
        String oldReviewers = "";
        for(ScienceField scienceField: magazine.getScienceFields()){
            oldScienceFields = oldScienceFields + scienceField.getName() + ", ";
        }
        for(Editor editor: magazine.getScienceFieldEditors()){
            oldEditors = oldEditors + editor.getFirstName() + " " + editor.getLastName() + ", ";
        }
        for(Reviewer reviewer: magazine.getReviewers()){
            oldReviewers = oldReviewers + reviewer.getFirstName() + " " + reviewer.getLastName() + ", ";
        }


        oldScienceFields = oldScienceFields.substring(0, oldScienceFields.length()-2);
        oldEditors = oldEditors.substring(0, oldEditors.length()-2);
        oldReviewers = oldReviewers.substring(0, oldReviewers.length()-2);


        delegateExecution.setVariable("oldScienceFields", oldScienceFields);
        delegateExecution.setVariable("oldReviewers", oldReviewers);
        delegateExecution.setVariable("oldEditors", oldEditors);
        delegateExecution.setVariable("naziv", magazine.getName());
        delegateExecution.setVariable("issn", magazine.getIssn());
        if(magazine.getBillingType().name().equals("AUTHORS")){
            delegateExecution.setVariable("nacinNaplacivanja", "Autorima");
        }else{
            delegateExecution.setVariable("nacinNaplacivanja", "Citaocima");
        }
    }

}