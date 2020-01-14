package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.dto.*;
import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.ScienceFieldService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.camunda.bpm.engine.task.Task;
import org.springframework.web.servlet.view.RedirectView;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "users")
public class UserController {

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
    private UserService userService;

    @Autowired
    private ScienceFieldService scienceFieldService;

    @RequestMapping(value = "/register/{taskId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationResponseDTO> register(@RequestBody List<FormSubmissionDto> registrationData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(registrationData);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        //runtimeService.setVariable(processInstanceId, "registrationData", dto);

        FormSubmissionDto userNameDto = null;
        for(FormSubmissionDto dto : registrationData){
            if(dto.getFieldId().equals("korisnicko_ime")){
                userNameDto = dto;
            }
        }

        User user = userService.findOneByUsername(userNameDto.getFieldValue());
        if(user != null){
            runtimeService.setVariable(processInstanceId, "dataValid", false);
            return new ResponseEntity<>(new RegistrationResponseDTO("Korisnik sa navedenim korisnickim imenom vec postoji."), HttpStatus.OK);
        }

        try{
            userService.validateData(registrationData);
        }catch(Exception e){
            runtimeService.setVariable(processInstanceId, "dataValid", false);
            return new ResponseEntity<>(new RegistrationResponseDTO(e.getMessage()), HttpStatus.OK);
        }

        // ako  su dobri podaci treba sacuvati korisnika u bazu i staviti mu da nije aktivan
        user = new User();
        user = userService.save(user,registrationData);

        if(user.isReviewer()){
            runtimeService.setVariable(processInstanceId, "isReviewer", true);
        }else{
            runtimeService.setVariable(processInstanceId, "isReviewer", true);
        }

        runtimeService.setVariable(processInstanceId, "userId", user.getUsername());
        formService.submitTaskForm(taskId, map);
        runtimeService.setVariable(processInstanceId, "dataValid", true);
        return new ResponseEntity<>(new RegistrationResponseDTO("Uspesno ste se registrovali! Proverite email kako bi verifikovali svoj profil."),HttpStatus.CREATED);
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDto> getFormFields(){
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Registracija");

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

    @RequestMapping(value = "/verify/{username}/{processId}", method = RequestMethod.GET)
    public RedirectView verify(@PathVariable("username") String username, @PathVariable("processId") String processId){
        RedirectView rv = new RedirectView();
        String hashedValue = (String) runtimeService.getVariable(processId, "hashedValue");
        boolean isValid = BCrypt.checkpw(username, hashedValue);
//        if(!isValid){
//
//        }
        runtimeService.setVariable(processId, "isVerified", true);
        rv.setUrl("http://localhost:4200/registration-success");
        return rv;
    }

    @RequestMapping(value = "/get-user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoDTO> getUser(HttpServletRequest request){
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        UserInfoDTO ui = new UserInfoDTO();
        if(username != "" && username != null) {
            User u = (User) userService.findOneByUsername(username);
            if(u instanceof Admin){
                u = (Admin) u;
                ui.setRole("ADMIN");
            }else if(u instanceof Reviewer){
                u = (Reviewer) u;
                ui.setRole("REVIEWER");
            }else if(u instanceof Editor){
                u = (Editor) u;
                ui.setRole("EDITOR");
            }
            ui.setUsername(u.getUsername());
            return new ResponseEntity<UserInfoDTO>(ui, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
