package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.dto.FormFieldsDto;
import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.dto.TaskDto;
import com.upp.naucnacentrala.model.BillingType;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.ScienceField;
import com.upp.naucnacentrala.model.User;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.ScienceFieldService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.CustomSQLExceptionTranslatorRegistrar;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/repository")
public class RepositoryController {

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

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private ScienceFieldService scienceFieldService;


    @RequestMapping(value = "/tasks/claim/{taskId}", method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity claim(@PathVariable String taskId, HttpServletRequest request) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String user = Utils.getUsernameFromRequest(request, tokenUtils);
        taskService.claim(taskId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/get/form/{taskId}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDto> getFormFields(@PathVariable("taskId") String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), processInstanceId, properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/form/check-magazine-data/{taskId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getCheckMagazineDataForm(@PathVariable("taskId") String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        Long magazineId = (Long) runtimeService.getVariable(task.getProcessInstanceId(), "magazineId");
        Magazine magazine = magazineService.findOneById(magazineId);

        for(FormField field : properties){
            if(field.getId().equals("naucne_oblasti_provera")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(ScienceField scienceField: magazine.getScienceFields()){
                    enumType.getValues().put(scienceField.getName(), scienceField.getName());
                }
            }
            if(field.getId().equals("urednici_provera")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(User user: magazine.getScienceFieldEditors()){
                    enumType.getValues().put(user.getUsername(), user.getFirstName() + " " + user.getLastName() + ", " + user.getUsername());
                }
            }
            if(field.getId().equals("recenzenti_provera")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(User user: magazine.getReviewers()){
                    enumType.getValues().put(user.getUsername(), user.getFirstName() + " " + user.getLastName() + ", " + user.getUsername());
                }
            }
        }

        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }


}
