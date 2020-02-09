package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.Author;
import com.upp.naucnacentrala.model.Editor;
import com.upp.naucnacentrala.service.MailService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyAuthorAndEditor implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Editor chiefEditor = (Editor) userService.findOneByUsername((String) delegateExecution.getVariable("chiefEditor"));
        Author author = (Author) userService.findOneByUsername((String) delegateExecution.getVariable("authorId"));
        mailService.sendNotification(author, chiefEditor);
    }
}
