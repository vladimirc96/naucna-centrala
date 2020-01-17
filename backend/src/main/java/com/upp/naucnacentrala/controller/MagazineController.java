package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.dto.FormFieldsDto;
import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.dto.TaskDto;
import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.ScienceFieldService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.FormFieldImpl;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import spinjar.com.fasterxml.jackson.databind.ser.std.EnumSerializer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "magazines")
public class MagazineController {
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
    private ScienceFieldService scienceFieldService;

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/form", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getForm(){
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Novi_casopis");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        List<ScienceField> scienceFields = scienceFieldService.findAll();
        for(FormField field : properties){
            if(field.getId().equals("naucne_oblasti")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(ScienceField scienceField: scienceFields){
                    enumType.getValues().put(scienceField.getName(), scienceField.getName());
                }
                break;
            }
        }

        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> save(@RequestBody List<FormSubmissionDto> magazineData, @PathVariable("taskId") String taskId, HttpServletRequest request){
        HashMap<String, Object> map = Utils.mapListToDto(magazineData);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "magazineData", magazineData);

        // urednik koji kreira novi casoppis postavlja se kao glavni urednik
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        runtimeService.setVariable(processInstanceId, "userId", username);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>("Casopis uspesno kreiran!", HttpStatus.OK);
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


    @RequestMapping(value = "/editorial-board/{taskId}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> saveEditorsAndReviewers(@RequestBody List<FormSubmissionDto> editorialBoardData, @PathVariable("taskId") String taskId, HttpServletRequest request){
        HashMap<String, Object> map = Utils.mapListToDto(editorialBoardData);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "editorialBoardData", editorialBoardData);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>("Urednici i rececenzenti uspesno postavljeni!", HttpStatus.OK);
    }

    @RequestMapping(value = "/tasks/magazine-correction", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<TaskDto>> getMagazineCorrectionTasks(HttpServletRequest request){
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Ispravka podataka casopisa").taskAssignee(username).list();

        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
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

    @RequestMapping(value = "/magazine-correction/{taskId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> magazineCorrection(@RequestBody List<FormSubmissionDto> magazineCorrectionData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(magazineCorrectionData);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "magazineCorrectionData", magazineCorrectionData);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>("Ispravka je uspesna!", HttpStatus.OK);
    }





}
