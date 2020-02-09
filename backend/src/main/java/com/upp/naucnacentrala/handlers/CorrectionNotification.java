package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.Author;
import com.upp.naucnacentrala.service.MailService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorrectionNotification implements JavaDelegate {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Author author = (Author) userService.findOneByUsername((String) delegateExecution.getVariable("authorId"));
        mailService.correctionNotification(author);
    }
}
