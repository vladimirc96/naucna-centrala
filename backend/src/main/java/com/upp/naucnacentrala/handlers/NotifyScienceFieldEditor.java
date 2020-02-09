package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.Editor;
import com.upp.naucnacentrala.service.MailService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyScienceFieldEditor implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Editor editor = (Editor) userService.findOneByUsername((String) delegateExecution.getVariable("scienceFieldEditor"));
        mailService.notifyScienceFieldEditor(editor);
    }
}
