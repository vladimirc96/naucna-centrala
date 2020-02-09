package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.dto.AccessAndMembershipDTO;
import com.upp.naucnacentrala.dto.FormFieldsDto;
import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.dto.MagazineDTO;
import com.upp.naucnacentrala.model.Coauthor;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.ScienceField;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.SciencePaperService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/science-paper")
public class SciencePaperController {

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
    private MagazineService magazineService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private SciencePaperService sciencePaperService;

    @RequestMapping(value = "/form", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getForm(HttpServletRequest request){
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Obrada_podnetog_teksta");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        runtimeService.setVariable(pi.getId(), "authorId", Utils.getUsernameFromRequest(request, tokenUtils));

        List<MagazineDTO> magazines = magazineService.findAll();
        for(FormField field : properties){
            if(field.getId().equals("casopis")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(MagazineDTO magazineDTO: magazines){
                    enumType.getValues().put(magazineDTO.getName(), magazineDTO.getName());
                }
                break;
            }
        }
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/select-magazine/{taskId}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<AccessAndMembershipDTO> selectMagazine(@RequestBody List<FormSubmissionDto> magazine, @PathVariable("taskId") String taskId, HttpServletRequest request){
        HashMap<String, Object> map = Utils.mapListToDto(magazine);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String name = Utils.getUsernameFromRequest(request, tokenUtils);
        runtimeService.setVariable(processInstanceId, "magazineName", magazine);
        runtimeService.setVariable(processInstanceId, "username", name);

        formService.submitTaskForm(taskId, map);
        boolean openAccess = (boolean) runtimeService.getVariable(processInstanceId, "open_access");
        boolean membership = (boolean) runtimeService.getVariable(processInstanceId, "uplacena_clanarina");
        return new ResponseEntity<>(new AccessAndMembershipDTO(openAccess, membership), HttpStatus.OK);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> save(@RequestBody List<FormSubmissionDto> sciencePaperData, @PathVariable("taskId") String taskId, HttpServletRequest request){
        HashMap<String, Object> map = Utils.mapListToDto(sciencePaperData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        SciencePaper sciencePaper = new SciencePaper();
        sciencePaper = sciencePaperService.save(sciencePaper);
        runtimeService.setVariable(processInstanceId, "sciencePaperData", sciencePaperData);
        runtimeService.setVariable(processInstanceId, "sciencePaperId", sciencePaper.getId());
        runtimeService.setVariable(processInstanceId, "coauthorList", new ArrayList<Coauthor>());
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(sciencePaper.getId().toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> savePdf(@RequestParam("file") MultipartFile file , @PathVariable("id") String sciencePaperId){
        SciencePaper sciencePaper = sciencePaperService.findOneById(Long.parseLong(sciencePaperId));
        sciencePaper = sciencePaperService.savePdf(file, sciencePaper);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @RequestMapping(value = "/paper-review/{taskId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> paperReview(@RequestBody List<FormSubmissionDto> reviewPaperData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(reviewPaperData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        formService.submitTaskForm(taskId, map);
        String value = Utils.getFormFieldValue(reviewPaperData, "relevantnost_rada");
        if(value.equals("ne")){
            return new ResponseEntity<>("Rad nije relevantan.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Rad je relevantan.", HttpStatus.OK);
    }

    @RequestMapping(value = "/pdf-download-url/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getPdfDownloadUrl(@PathVariable("processInstanceId") String processInstanceId, HttpServletRequest request){
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) runtimeService.getVariable(processInstanceId, "sciencePaperId"));
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/science-paper/download/")
        .path(sciencePaper.getId().toString())
        .toUriString();
        return new ResponseEntity<>(fileDownloadUri, HttpStatus.OK);
    }

    @RequestMapping(value = "/download/{sciencePaperId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Resource> downloadFile(@PathVariable("sciencePaperId") String sciecnePaperId) {
        System.out.println("************************************************");
        System.out.println("DOWNLOAD PDF METODA");
        System.out.println("************************************************");
        SciencePaper sciencePaper = sciencePaperService.findOneById(Long.parseLong(sciecnePaperId));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sciencePaper.getTitle() + "\"")
                .body(new ByteArrayResource(sciencePaper.getPdf()));
    }

}
