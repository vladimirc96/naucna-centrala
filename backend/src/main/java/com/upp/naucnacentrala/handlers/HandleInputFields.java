package com.upp.naucnacentrala.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

@Service
public class HandleInputFields implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        delegateExecution.setVariable("preporuka_glavnog_urednika", "");
        delegateExecution.setVariable("komentar_glavnog_urednika",  "");
    }
}
