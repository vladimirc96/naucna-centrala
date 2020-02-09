package com.upp.naucnacentrala.controller;

import com.sun.xml.internal.ws.resources.HttpserverMessages;
import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.dto.FormFieldsDto;
import com.upp.naucnacentrala.dto.TaskDto;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.ScienceField;
import com.upp.naucnacentrala.model.User;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.ScienceFieldService;
import com.upp.naucnacentrala.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
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

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/tasks/claim/{taskId}", method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity claim(@PathVariable String taskId, HttpServletRequest request) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String user = Utils.getUsernameFromRequest(request, tokenUtils);
        taskService.claim(taskId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/form/{taskId}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
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
                if(!enumType.getValues().isEmpty()) enumType.getValues().clear();
                for(ScienceField scienceField: magazine.getScienceFields()){
                    enumType.getValues().put(scienceField.getName(), scienceField.getName());
                }
            }
            if(field.getId().equals("urednici_provera")){
                EnumFormType enumType = (EnumFormType) field.getType();
                if(!enumType.getValues().isEmpty()) enumType.getValues().clear();
                for(User user: magazine.getScienceFieldEditors()){
                    enumType.getValues().put(user.getUsername(), user.getFirstName() + " " + user.getLastName() + ", " + user.getUsername());
                }
            }
            if(field.getId().equals("recenzenti_provera")){
                EnumFormType enumType = (EnumFormType) field.getType();
                if(!enumType.getValues().isEmpty()) enumType.getValues().clear();
                for(User user: magazine.getReviewers()){
                    enumType.getValues().put(user.getUsername(), user.getFirstName() + " " + user.getLastName() + ", " + user.getUsername());
                }
            }
        }

        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/form/magazine-correction/{taskId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getMagazineCorrectionForm(@PathVariable("taskId") String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        String billing = (String) runtimeService.getVariable(pi.getId(), "nacinNaplacivanja");
        List<User> editors = userService.findAllEditors();
        List<User> reviewers = userService.findAllReviewers();
        List<ScienceField> fields = scienceFieldService.findAll();
        for(FormField field : properties){
            if(field.getId().equals("nacin_naplacivanja_stari")){
                EnumFormType enumFormType = (EnumFormType) field.getType();
                List<String> keys = new ArrayList<>(enumFormType.getValues().values());
                for(String val: keys){
                    if (val.equals(billing)){
                        field.getProperties().put(val, "selected");
                    }
                }
            }
            if(field.getId().equals("naucne_oblasti_ispravka")){
                EnumFormType scienceFields = (EnumFormType) field.getType();
                for(ScienceField scienceField: fields){
                    scienceFields.getValues().put(scienceField.getName(), scienceField.getName());
                }
            }
            if(field.getId().equals("urednici_ispravka")){
                EnumFormType urednici = (EnumFormType) field.getType();
                for(User user: editors){
                    urednici.getValues().put(user.getUsername(), user.getFirstName() + " " + user.getLastName() + ", " + user.getUsername());
                }
            }
            if(field.getId().equals("recenzenti_ispravka")){
                EnumFormType urednici = (EnumFormType) field.getType();
                for(User user: reviewers){
                    urednici.getValues().put(user.getUsername(), user.getFirstName() + " " + user.getLastName() + ", " + user.getUsername());
                }
            }
        }

        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/form/editorial-board/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getEditorialBoardForm(@PathVariable("processInstanceId") String processInstanceId){
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        Long magazineId = (Long) runtimeService.getVariable(processInstanceId, "magazineId");
        Magazine magazine = magazineService.findOneById(magazineId);

        List<User> reviewers = userService.findAllByMagazineScienceFields(magazine.getScienceFields(), "REVIEWER");
        for(FormField field : properties){
            if(field.getId().equals("recenzenti")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(User user: reviewers){
                    enumType.getValues().put(user.getUsername(), user.getFirstName() + " " + user.getLastName() + ", " + user.getUsername());
                }
                break;
            }
        }
        List<User> editors = userService.findAllByMagazineScienceFields(magazine.getScienceFields(), "EDITOR");
        for(FormField field : properties){
            if(field.getId().equals("urednici")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(User user: editors){
                    enumType.getValues().put(user.getUsername(), user.getFirstName() + " " + user.getLastName() + ", " + user.getUsername());
                }
                break;
            }
        }

        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/form/science-paper/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getSciencePaperForm(@PathVariable("processInstanceId") String processInstanceId){
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        List<ScienceField> scienceFields = scienceFieldService.findAll();
        for(FormField field : properties){
            if(field.getId().equals("naucna_oblast")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(ScienceField scienceField: scienceFields){
                    enumType.getValues().put(scienceField.getName(), scienceField.getName());
                }
            }
        }
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/tasks/coauthor", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity addCoauthorTasks(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Koautori").taskAssignee(username).list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/tasks/review-paper", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity reviewPaperTasks(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Pregled rada").taskAssignee(username).list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }
}
