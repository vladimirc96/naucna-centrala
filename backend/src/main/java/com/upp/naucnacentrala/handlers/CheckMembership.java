package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Author;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckMembership implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<FormSubmissionDto> formSubmissionDto = (List<FormSubmissionDto>) delegateExecution.getVariable("magazineName");
        String username = (String) delegateExecution.getVariable("username");
        Magazine magazine = magazineService.findByName(formSubmissionDto.iterator().next().getFieldValue());
        delegateExecution.setVariable("uplacena_clanarina", false);
        for(Author a: magazine.getAuthors()){
            if(a.getUsername().equals(username)){
                delegateExecution.setVariable("uplacena_clanarina", true);
            }
        }
    }
}
