package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.dto.TaskDto;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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



}
