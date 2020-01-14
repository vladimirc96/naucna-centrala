package com.upp.naucnacentrala.handlers;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationHandler implements ExecutionListener {

    @Autowired
    IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        List<Group> groups = identityService.createGroupQuery().groupIdIn("recenzenti", "admin", "urednici").list();
        if(groups.isEmpty()) {
            Group recenzentiGroup = identityService.newGroup("recenzenti");
            recenzentiGroup.setId("recenzenti");
            recenzentiGroup.setName("recenzenti");
            identityService.saveGroup(recenzentiGroup);

            Group adminGroup = identityService.newGroup("admin");
            adminGroup.setId("admin");
            adminGroup.setName("admin");
            identityService.saveGroup(adminGroup);

            Group uredniciGroup = identityService.newGroup("urednici");
            uredniciGroup.setId("urednici");
            uredniciGroup.setName("urednici");
            identityService.saveGroup(uredniciGroup);


        }
        User temp = identityService.createUserQuery().userId("dovla").singleResult();
        if(temp == null) {
            User user = identityService.newUser("dovla");
            user.setEmail("cvetanovic9696@gmail.com");
            user.setPassword("dovla");
            user.setFirstName("Vladimir");
            user.setLastName("Cvetanovic");
            user.setId("dovla");
            identityService.saveUser(user);
            identityService.createMembership(user.getId(), "admin");
        }
    }
}
