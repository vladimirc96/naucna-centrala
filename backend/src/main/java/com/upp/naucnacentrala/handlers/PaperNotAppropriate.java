package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.Author;
import com.upp.naucnacentrala.service.MailService;
import com.upp.naucnacentrala.service.SciencePaperService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;

@Service
public class PaperNotAppropriate implements JavaDelegate {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private SciencePaperService sciencePaperService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Author author = (Author) userService.findOneByUsername((String) delegateExecution.getVariable("authorId"));
        mailService.paperNotAppropriate(author);
        sciencePaperService.remove(sciencePaperService.findOneById((Long) delegateExecution.getVariable("sciencePaperId")));
    }
}
