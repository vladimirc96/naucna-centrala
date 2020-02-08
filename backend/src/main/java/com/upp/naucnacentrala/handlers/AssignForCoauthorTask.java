package com.upp.naucnacentrala.handlers;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

public class AssignForCoauthorTask implements JavaDelegate {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Authentication authentication = identityService.getCurrentAuthentication();
        System.out.println("**************************************");
        System.out.println("POSTAVLJANJE TASKA ZA KOAUTORE. AUTHOR ASSIGNEE: " + authentication.getUserId());
        System.out.println("**************************************");
        Task coauthorTask = taskService.createTaskQuery().processInstanceId(delegateExecution.getProcessInstanceId()).singleResult();
        coauthorTask.setAssignee(authentication.getUserId());
        taskService.saveTask(coauthorTask);
    }
}
