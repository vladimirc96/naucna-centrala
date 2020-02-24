package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.client.GoogleClient;
import com.upp.naucnacentrala.dto.*;
import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.service.SciencePaperService;
import com.upp.naucnacentrala.service.UserService;
import com.upp.naucnacentrala.service.elasticsearch.ReviewerESService;
import com.upp.naucnacentrala.service.elasticsearch.SciencePaperESService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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

    @Autowired
    private UserService userService;

    @Autowired
    private GoogleClient googleClient;

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
    private ResponseEntity<SciencePaperDTO> save(@PathVariable("sciencePaperId") Long id) throws UnsupportedEncodingException {
        SciencePaper sciencePaper = sciencePaperService.findOneById(id);
        // sacuvaj u elastic-u rad
        SciencePaperES sciencePaperES = new SciencePaperES();
        String text = sciencePaperESService.parsePDF(sciencePaper);
        sciencePaperES.setText(text);
        sciencePaperES.setId(sciencePaper.getId().toString());
        sciencePaperES.setKeyTerms(sciencePaper.getKeyTerm());
        sciencePaperES.setTitle(sciencePaper.getTitle());
        sciencePaperES.setPaperAbstract(sciencePaper.getPaperAbstract());
        sciencePaperES.setScienceField(sciencePaper.getScienceField().getName());
        sciencePaperES.setMagazineName(sciencePaper.getMagazine().getName());
        sciencePaperES.setFilePath(sciencePaperService.getPath(sciencePaper.getId()));
        Random rand = new Random();
        int prefix = rand.nextInt(1000) + 1;
        int suffix = rand.nextInt(1000) + 1;
        sciencePaperES.setDoi("10." + prefix + "/" + suffix);
        Author author = (Author) userService.findOneByUsername(sciencePaper.getAuthor().getUsername());
        sciencePaperES.setAuthor(author.getFirstName() + " " + author.getLastName());


        sciencePaperES = sciencePaperESService.save(sciencePaperES);
        SciencePaperDTO sciencePaperDTO = new SciencePaperDTO();
        sciencePaperDTO.setId(Long.parseLong(sciencePaperES.getId()));
        sciencePaperDTO.setMagazine(new MagazineInfoDTO());
        sciencePaperDTO.setTitle(sciencePaperES.getTitle());
        sciencePaperDTO.setKeyTerm(sciencePaperES.getKeyTerms());
        sciencePaperDTO.setPaperAbstract(sciencePaperES.getPaperAbstract());

        List<ReviewerES> reviewerESList = new ArrayList<>();
        for(Reviewer reviewer: sciencePaper.getReviewers()){
            ReviewerES reviewerESTemp = reviewerESService.findOneById(reviewer.getUsername());
            if(reviewerESTemp == null){
                ReviewerES reviewerES = new ReviewerES();
                reviewerES.setScienceFields(reviewer.getScienceFields());
                reviewerES.setFirstName(reviewer.getFirstName());
                reviewerES.setLastName(reviewer.getLastName());
                reviewerES.setEmail(reviewer.getEmail());
                reviewerES.setId(reviewer.getUsername());
                reviewerES.getSciencePapers().add(sciencePaperES);
                Location location = googleClient.getCoordinates(reviewer.getCity());
                reviewerES.setLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
                reviewerES = reviewerESService.save(reviewerES);
            }else{
                reviewerESTemp.getSciencePapers().add(sciencePaperES);
                reviewerESTemp = reviewerESService.save(reviewerESTemp);
            }
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
        sciencePaperDTO.setPaperAbstract(sciencePaperES.getPaperAbstract());
        return new ResponseEntity<>(sciencePaperDTO, HttpStatus.OK);
    }

//    @RequestMapping(value = "/getCoords", method = RequestMethod.GET, produces = "application/json")
//    private ResponseEntity getCoords(){
//        return googleClient.getCoordinates("Novi Sad");
//    }

}
