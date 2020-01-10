package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.User;
import com.upp.naucnacentrala.repository.UserRepository;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CheckRegistrationData implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        delegateExecution.setVariable("isVerified", false);


        System.out.println("************ Provera podataka ************");

        List<FormSubmissionDto> registrationData = (List<FormSubmissionDto>) delegateExecution.getVariable("registrationData");
        FormSubmissionDto userNameDto = null;
        for(FormSubmissionDto dto : registrationData){
            if(dto.getFieldId().equals("korisnicko_ime")){
                userNameDto = dto;
            }
        }

        User user = userService.findOneByUsername(userNameDto.getFieldValue());
        if(user != null){
            System.out.print("**************** REGISTER ERROR: Username already exists!");
            return;
        }

//        // provera podataka registracije
//        if(!userService.isDataValid(registrationData)){
//            System.out.print("**************** REGISTER ERROR: Registration data not valid!");
//            return;
//        }

        // ako  su dobri podaci treba sacuvati korisnika u bazu i staviti mu da nije aktivan
        user = new User();
        user = userService.save(user,registrationData);

        if(user.isReviewer()){
            delegateExecution.setVariable("isReviewer", true);
        }else{
            delegateExecution.setVariable("isReviewer", false);
        }

        delegateExecution.setVariable("userId", user.getUsername());
        System.out.println("************ Provera podataka ************");
    }


}
