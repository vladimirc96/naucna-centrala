package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.dto.FormFieldsDto;
import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.dto.TaskDto;
import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.RoleService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.impl.form.type.StringFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.value.builder.TypedValueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "admin")
public class AdminController {

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
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MagazineService magazineService;

    @RequestMapping(value= "/tasks/reviewer", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<TaskDto>> getTasksReviewer(){
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("admin").list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            if(task.getName().equals("Potvrda recenzenta")) {
                TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
                tasksDto.add(t);
            }
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }

    @RequestMapping(value= "/set-reviewer/{taskId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> setReviewer(@RequestBody List<FormSubmissionDto> reviewer, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(reviewer);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "reviewerData", reviewer);

        for(FormSubmissionDto dto: reviewer){
            if(dto.getFieldId().equals("potvrda")){
                if(dto.getFieldValue().equals("da")){
                    runtimeService.setVariable(processInstanceId, "isReviewerSet", true);
                }else{
                    runtimeService.setVariable(processInstanceId, "isReviewerSet", false);
                }
                break;
            }
        }

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>("Task obavljen!", HttpStatus.OK);
    }

    @RequestMapping(value= "/tasks/check-magazine-data", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<TaskDto>> getTasksCheckMagazineData(){
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("admin").list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            if(task.getName().equals("Provera podataka casopisa")) {
                TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
                tasksDto.add(t);
            }
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }


    @RequestMapping(value = "/check-magazine-data/{taskId}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> checkMagazineDataSubmit(@RequestBody List<FormSubmissionDto> checkMagazineData,@PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(checkMagazineData);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        for(FormSubmissionDto formDto: checkMagazineData){
            if(formDto.getFieldId().equals("ispravka")){
                if(formDto.getFieldValue().equals("true")){
                    runtimeService.setVariable(processInstanceId, "magazineCorrection", true);
                }else{
                    runtimeService.setVariable(processInstanceId, "magazineCorrection", false);
                    formService.submitTaskForm(taskId, map);
                    return new ResponseEntity<>("Task obavljen!", HttpStatus.OK);
                }
            }
        }
        formService.submitTaskForm(taskId, map);

        Magazine magazine = magazineService.findOneById((Long) runtimeService.getVariable(processInstanceId, "magazineId"));
        Task correctionTask = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        correctionTask.setAssignee(magazine.getChiefEditor().getUsername());
        taskService.saveTask(correctionTask);

        return new ResponseEntity<>("Task obavljen!", HttpStatus.OK);
    }



}
