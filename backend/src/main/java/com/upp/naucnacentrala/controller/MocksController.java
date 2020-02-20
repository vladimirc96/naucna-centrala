package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.dto.*;
import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.service.SciencePaperService;
import com.upp.naucnacentrala.service.elasticsearch.ReviewerESService;
import com.upp.naucnacentrala.service.elasticsearch.SciencePaperESService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "/mocks")
public class MocksController {

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
    private SciencePaperService sciencePaperService;

    @Autowired
    private SciencePaperESService sciencePaperESService;

    @Autowired
    private ReviewerESService reviewerESService;

    @RequestMapping(value = "/payment/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getForm(@PathVariable("processInstanceId") String processInstanceId){
        ProcessInstance subprocess = runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstanceId).singleResult();
        Task task = taskService.createTaskQuery().processInstanceId(subprocess.getId()).list().get(0);
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), subprocess.getId(), properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> submit(@RequestBody List<FormSubmissionDto> paymentData, @PathVariable("taskId") String taskId, HttpServletRequest request){
        HashMap<String, Object> map = Utils.mapListToDto(paymentData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    // cuvanje rada na ES
    @RequestMapping(value = "/savePaper/{sciencePaperId}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    private ResponseEntity<SciencePaperDTO> save(@PathVariable("sciencePaperId") Long id){
        SciencePaper sciencePaper = sciencePaperService.findOneById(id);
        List<Coauthor> coauthorList = new ArrayList<>();
        for(Coauthor coauthor: sciencePaper.getCoauthors()){
            coauthorList.add(coauthor);
        }

        System.out.println("*********************");
        System.out.println("INICIjALIZACIJA ES OBJEKTA");
        // sacuvaj u elastic-u rad
        SciencePaperES sciencePaperES = new SciencePaperES();
        sciencePaperES.setCoauthors(coauthorList);
        sciencePaperES.setId(sciencePaper.getId().toString());
        sciencePaperES.setKeyTerms(sciencePaper.getKeyTerm());
        sciencePaperES.setTitle(sciencePaper.getTitle());
        sciencePaperES.setPaperAbastract(sciencePaper.getPaperAbstract());
        sciencePaperES.setScienceField(sciencePaper.getScienceField().getName());
        sciencePaperES.setMagazineName(sciencePaper.getMagazine().getName());
        sciencePaperES.setFilePath(sciencePaperService.getPath(sciencePaper.getId()));
        Random rand = new Random();
        int prefix = rand.nextInt(1000) + 1;
        int suffix = rand.nextInt(1000) + 1;
        sciencePaperES.setDoi("10." + prefix + "/" + suffix);

        sciencePaperES = sciencePaperESService.save(sciencePaperES);
        SciencePaperDTO sciencePaperDTO = new SciencePaperDTO();
        sciencePaperDTO.setId(Long.parseLong(sciencePaperES.getId()));
        sciencePaperDTO.setMagazine(new MagazineInfoDTO());
        sciencePaperDTO.setTitle(sciencePaperES.getTitle());
        sciencePaperDTO.setKeyTerm(sciencePaperES.getKeyTerms());
        sciencePaperDTO.setPaperAbstract(sciencePaperES.getPaperAbastract());

        Magazine magazine = sciencePaper.getMagazine();
        List<ReviewerES> reviewerESList = new ArrayList<>();
        for(Reviewer reviewer: magazine.getReviewers()){
            System.out.println("***************************************");
            System.out.println("REVIWER ID: " + reviewer.getUsername());
            System.out.println("SCIENCEFIELD LIST: " + reviewer.getScienceFields().size());
            System.out.println("***************************************");
            ReviewerES reviewerES = new ReviewerES();
            reviewerES.setScienceFields(reviewer.getScienceFields());
            reviewerES.setFirstName(reviewer.getFirstName());
            reviewerES.setLastName(reviewer.getLastName());
            reviewerES.setEmail(reviewer.getEmail());
            reviewerES.setId(reviewer.getUsername());
            reviewerES.getSciencePapers().add(sciencePaperES);
            reviewerES.setLocation(null);
            reviewerES = reviewerESService.save(reviewerES);
        }

        return new ResponseEntity<>(sciencePaperDTO, HttpStatus.OK);
    }
    @RequestMapping(value = "/getPaper/{sciencePaperId}", method = RequestMethod.GET, produces = "application/json")
    private ResponseEntity<SciencePaperDTO> getPaper(@PathVariable("sciencePaperId") String id){
        SciencePaperES sciencePaperES = sciencePaperESService.findOneById(id);
        SciencePaperDTO sciencePaperDTO = new SciencePaperDTO();
        sciencePaperDTO.setId(Long.parseLong(sciencePaperES.getId()));
        sciencePaperDTO.setMagazine(new MagazineInfoDTO());
        sciencePaperDTO.setTitle(sciencePaperES.getTitle());
        sciencePaperDTO.setKeyTerm(sciencePaperES.getKeyTerms());
        sciencePaperDTO.setPaperAbstract(sciencePaperES.getPaperAbastract());
        return new ResponseEntity<>(sciencePaperDTO, HttpStatus.OK);
    }

}
