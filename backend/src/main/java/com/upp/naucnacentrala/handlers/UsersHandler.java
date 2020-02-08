package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.Admin;
import com.upp.naucnacentrala.model.Editor;
import com.upp.naucnacentrala.model.Reviewer;
import com.upp.naucnacentrala.model.User;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersHandler implements ExecutionListener {

    @Autowired
    private UserService userService;

    @Autowired
    private IdentityService identityService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        System.out.println("****************************************");
        System.out.println("USER HANDLER");

        List<User> userList = userService.findAll();

        for(User user: userList){
            System.out.println("USER IZ BAZE: " + user.getUsername());
            org.camunda.bpm.engine.identity.User temp = identityService.createUserQuery().userId(user.getUsername()).singleResult();
            if(temp == null) {
                System.out.println("****************************************");
                System.out.println("DODAVANJE NOVOG KORISNIKA U CAMUNDA BAZU: " + user.getFirstName() + " " + user.getLastName());

                org.camunda.bpm.engine.identity.User newUser = identityService.newUser(user.getUsername());
                newUser.setEmail(user.getEmail());
                newUser.setPassword(user.getPassword());
                newUser.setFirstName(user.getFirstName());
                newUser.setLastName(user.getLastName());
                newUser.setId(user.getUsername());
                identityService.saveUser(newUser);
                if(user instanceof Reviewer){
                    identityService.createMembership(newUser.getId(), "recenzenti");
                }else if(user instanceof Admin){
                    identityService.createMembership(newUser.getId(), "admin");
                }else if(user instanceof Editor){
                    identityService.createMembership(newUser.getId(), "urednici");
                }
                System.out.println("****************************************");
            }
        }

    }
}
