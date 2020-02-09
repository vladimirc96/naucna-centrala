package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.service.MagazineService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetChiefEditor implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String magazineName = (String) delegateExecution.getVariable("magazineName");
        Magazine magazine = magazineService.findByName(magazineName);
        delegateExecution.setVariable("chiefEditor", magazine.getChiefEditor().getUsername());
    }

}
