package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Coauthor;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.service.CoauthorService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/coauthor")
public class CoauthorController {

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
    private CoauthorService coauthorService;

    @RequestMapping( value = "/{taskId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> save(@RequestBody List<FormSubmissionDto> coauthorData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(coauthorData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        ArrayList<Coauthor> coauthors = (ArrayList<Coauthor>) runtimeService.getVariable(processInstanceId,"coauthorList");
        Coauthor coauthor = coauthorService.create(coauthorData);
        coauthors.add(coauthor);
        runtimeService.setVariable(processInstanceId, "coauthorList", coauthors);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
