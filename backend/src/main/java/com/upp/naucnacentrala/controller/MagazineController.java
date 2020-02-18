package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.dto.FormFieldsDto;
import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.dto.MagazineDTO;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import spinjar.com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import java.lang.reflect.Member;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    RestTemplate restTemplate;


    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<MagazineDTO>> getAll(){
        return new ResponseEntity<>(magazineService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-by-editor", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<MagazineDTO>> findAllByChiefEditor(HttpServletRequest request){
        return new ResponseEntity(magazineService.findAllByChiefEditor(Utils.getUsernameFromRequest(request, tokenUtils)), HttpStatus.OK);
    }

    @RequestMapping(value = "/{magazineId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<MagazineDTO> get(@PathVariable("magazineId") String magazineId){
        return new ResponseEntity<>(magazineService.findOneDto(Long.parseLong(magazineId)), HttpStatus.OK);
    }

    @RequestMapping(value = "/isSubbed/{magazineId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> checkIfSubbed(@PathVariable("magazineId") Long magazineId, HttpServletRequest request) throws ParseException {
        Magazine magazine = magazineService.findOneById(magazineId);
        String korisnik = Utils.getUsernameFromRequest(request, tokenUtils);

        for(Membership membership : magazine.getMemberships()) {
            if(membership.getUsername().equals(korisnik)) {
                if(checkDates(membership.getEndDate())) {
                    if(membership.getAgreementId() == 0) {
                        return new ResponseEntity<String>("Subbed", HttpStatus.OK);
                    } else {
                        ResponseEntity response = restTemplate.getForEntity("https://192.168.43.124:8500/paypal-service/paypal/AgreementExistsOnPP/" + membership.getAgreementId(),
                                String.class);
                        String retVal = (String) response.getBody();
                        if(retVal.equals("exists")) {
                            return new ResponseEntity<String>("Subbed", HttpStatus.OK);
                        } else {
                            magazine.getMemberships().remove(membership);
                            magazineService.save(magazine);
                            return new ResponseEntity<String>("notSubbed", HttpStatus.OK);
                        }
                    }
                } else {
                    magazine.getMemberships().remove(membership);
                    magazineService.save(magazine);
                    return new ResponseEntity<String>("notSubbed", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<String>("notSubbed", HttpStatus.OK);
    }


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

    @RequestMapping(value = "/magazine-correction/{taskId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> magazineCorrection(@RequestBody List<FormSubmissionDto> magazineCorrectionData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(magazineCorrectionData);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "magazineCorrectionData", magazineCorrectionData);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>("Ispravka je uspesna!", HttpStatus.OK);
    }

    public boolean checkDates(String dateClanarine) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dt = sdf.format(date);

        Date today = sdf.parse(dt);
        Date sub = sdf.parse(dateClanarine);
        if(today.compareTo(sub) > 0) {
            return false;
        }
        return true;
    }


}
