package com.upp.naucnacentrala.handlers;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.upp.naucnacentrala.dto.EnumVal;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.spin.Spin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.camunda.spin.Spin.JSON;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.variable.Variables.objectValue;

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
        // uzmi listu taskova
        System.out.println("******************** USAO U DELEGATE");
        Map<String, String> fields = new HashMap<String,String>() {{
           put("matematika", "Matematika");
           put("informatika", "Informatika");
        }};
        ObjectValue vals = Variables.objectValue(fields).serializationDataFormat(Variables.SerializationDataFormats.JAVA).create();
        // String vals = new ObjectMapper().writeValueAsString(fields);
        delegateExecution.setVariable("scienceFields", vals);
    }
}
