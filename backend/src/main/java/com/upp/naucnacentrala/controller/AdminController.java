package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.dto.FormFieldsDto;
import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.dto.TaskDto;
import com.upp.naucnacentrala.model.Reviewer;
import com.upp.naucnacentrala.model.Role;
import com.upp.naucnacentrala.model.User;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.RoleService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
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

    @RequestMapping(value= "/get/tasks/reviewer", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasAuthority('SET_REVIEWER_TASK')")
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

    @RequestMapping(value = "/tasks/claim/{taskId}", method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity claim(@PathVariable String taskId, HttpServletRequest request) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String user = getUsernameFromRequest(request);
        taskService.claim(taskId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/reviewer/form/{taskId}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDto> getFormFields(@PathVariable("taskId") String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), processInstanceId, properties), HttpStatus.OK);
    }

    @RequestMapping(value= "/set-reviewer/{taskId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> setReviewer(@RequestBody List<FormSubmissionDto> reviewer, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = mapListToDto(reviewer);

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


    private String getUsernameFromRequest(HttpServletRequest request) {
        String authToken = tokenUtils.getToken(request);
        if (authToken == null) {
            return null;
        }
        String username = tokenUtils.getUsernameFromToken(authToken);
        return username;
    }

    private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDto temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

}
