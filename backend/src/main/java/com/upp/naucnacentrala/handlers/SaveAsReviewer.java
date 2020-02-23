package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Reviewer;
import com.upp.naucnacentrala.model.User;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaveAsReviewer implements JavaDelegate {

    @Autowired
    IdentityService identityService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<FormSubmissionDto> reviewer = (List<FormSubmissionDto>) delegateExecution.getVariable("reviewerData");
        Reviewer user = userService.saveAsReviewer(reviewer);
        identityService.createMembership(user.getUsername(), "recenzenti");
    }
}
