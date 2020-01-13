package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.User;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivateUser implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private IdentityService identityService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        User user = userService.findOneByUsername((String) delegateExecution.getVariable("userId"));
        user.setActive(true);
        user = userService.save(user);
        delegateExecution.setVariable("isActive", true);

        // dodaj korisnika u camundu
        org.camunda.bpm.engine.identity.User newUser = identityService.newUser(user.getUsername());
        newUser.setId(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        identityService.saveUser(newUser);
    }
}
