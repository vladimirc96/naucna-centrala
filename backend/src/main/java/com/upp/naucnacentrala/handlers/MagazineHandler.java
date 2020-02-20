package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.ScienceField;
import com.upp.naucnacentrala.service.ScienceFieldService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MagazineHandler implements ExecutionListener {

    @Autowired
    IdentityService identityService;

    @Autowired
    private ScienceFieldService scienceFieldService;

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

        User temp1 = identityService.createUserQuery().userId("vukasin").singleResult();
        if(temp1 == null){
            User user = identityService.newUser("vukasin");
            user.setEmail("vukasin@gmail.com");
            user.setPassword("vukasin96");
            user.setFirstName("Vukasin");
            user.setLastName("Jovic");
            user.setId("vukasin");
            identityService.saveUser(user);
            identityService.createMembership(user.getId(), "recenzenti");
        }

    }
}
