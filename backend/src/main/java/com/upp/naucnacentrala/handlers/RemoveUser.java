package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.User;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoveUser implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        User user = userService.findOneByUsername((String)delegateExecution.getVariable("userId"));
        userService.remove(user);
        delegateExecution.setVariable("isUserDeleted", true);
    }
}
