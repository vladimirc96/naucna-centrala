package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Editor;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavaMagazine implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<FormSubmissionDto> magazineData = (List<FormSubmissionDto>) delegateExecution.getVariable("magazineData");
        Magazine magazine = magazineService.save(magazineData);
        delegateExecution.setVariable("magazineId", magazine.getId());
    }



}
