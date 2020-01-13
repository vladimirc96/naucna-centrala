package com.upp.naucnacentrala.handlers;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetReviewerConfirmationTask implements JavaDelegate {

    @Autowired
    IdentityService identityService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //Group group = (Group) identityService.createGroupQuery().groupId("admin").singleResult();
        List<User> adminUsers = identityService.createUserQuery().memberOfGroup("admin").list();
        for(User user: adminUsers){

        }
    }

}
