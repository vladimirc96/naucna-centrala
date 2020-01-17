package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.repository.ScienceFieldRepository;
import com.upp.naucnacentrala.repository.UserRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.joda.time.ReadableInstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Autowired
    private ScienceFieldService scienceFieldService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleService roleService;

    public User save(User user){
        return userRepo.save(user);
    }

    public User save(User user, List<FormSubmissionDto> registrationData){
        List<ScienceField> fields = new ArrayList<>();
        for(FormSubmissionDto dto : registrationData){

            if(dto.getFieldId().equals("ime")){
                user.setFirstName(dto.getFieldValue());
            }else if(dto.getFieldId().equals("prezime")){
                user.setLastName(dto.getFieldValue());
            }else if(dto.getFieldId().equals("grad")){
                user.setCity(dto.getFieldValue());
            }else if(dto.getFieldId().equals("drzava")){
                user.setCountry(dto.getFieldValue());
            }else if(dto.getFieldId().equals("email")){
                user.setEmail(dto.getFieldValue());
            }else if(dto.getFieldId().equals("korisnicko_ime")){
                user.setUsername(dto.getFieldValue());
            }else if(dto.getFieldId().equals("sifra")){
                // generisati salt pomocu BCrypta i uraditi hesiranje sifre
                String salt = BCrypt.gensalt();
                String hashedPass = BCrypt.hashpw(dto.getFieldValue(), salt);
                user.setPassword(hashedPass);
            }else if(dto.getFieldId().equals("recenzent")) {

                if(dto.getFieldValue() != null) {
                    if (dto.getFieldValue().equals("true")) {
                        user.setReviewer(true);
                    }else{
                        user.setReviewer(false);
                    }
                }else{
                    user.setReviewer(false);
                }
            }else if(dto.getFieldId().equals("naucne_oblasti")){
                ScienceField field = scienceFieldService.findOneByName(dto.getFieldValue());
                fields.add(field);
            }

        }
        user.setScienceFields(fields);
        user.setActive(false);
        Role role = roleService.findOneByName("ROLE_USER");
        List<Role> roles = Arrays.asList(role);
        user.setRoles(roles);
        return userRepo.save(user);
    }

    public User saveAsReviewer(List<FormSubmissionDto> reviewer){
        FormSubmissionDto firstName = null;
        FormSubmissionDto lastName = null;
        FormSubmissionDto choice = null;
        for(FormSubmissionDto dto: reviewer){
            if(dto.getFieldId().equals("ime_potvrda")) firstName = dto;
            if(dto.getFieldId().equals("prezime_potvrda")) lastName = dto;
            if(dto.getFieldId().equals("potvrda")) choice = dto;
        }

        User user = userRepo.findOneByFirstNameAndLastName(firstName.getFieldValue(),lastName.getFieldValue());
        if(choice.getFieldValue().equals("da")) {
            List<ScienceField> fields = user.getScienceFields();
            userRepo.delete(user);
            user = new Reviewer(user);
            Role role = roleService.findOneByName("ROLE_REVIEWER");
            List<Role> roles = Arrays.asList(role);
            user.setRoles(roles);
            user.setScienceFields(fields);
            System.out.println("****************************** SIZE:" + fields.size());
            user = userRepo.save(user);
        }
        return user;
    }


    public void validateData(List<FormSubmissionDto> registrationData) throws Exception {
        for(FormSubmissionDto dto : registrationData){
            if(dto.getFieldId().equals("email")){
                if(!mailValid(dto.getFieldValue())) throw new Exception("Email nije validan.");
            }else if(dto.getFieldId().equals("korisnicko_ime")){
                if(!usernameValid(dto.getFieldValue())) throw new Exception("Korisnicko ime nije validno.");
            }else if(dto.getFieldId().equals("sifra")) {
                if (!passValid(dto.getFieldValue())) throw new Exception("Sifra nije validna. Mora da sadrzi minimum 8 karaktera, jedno slovo i jedan broj.");
            }
        }
    }

    public User findOneByUsername(String username){
        return userRepo.findOneByUsername(username);
    }

    public User findOneByFirstNameAndLastName(String firstname, String lastname){
        return userRepo.findOneByFirstNameAndLastName(firstname, lastname);
    }


    public List<User> findAll(){
        return userRepo.findAll();
    }

    public void remove(User user){
        userRepo.delete(user);
    }

    public void removeById(String username){
        userRepo.deleteById(username);
    }

    public void sendNotificationSync(User user, String processId) throws MailException, InterruptedException {
        SimpleMailMessage mail = new SimpleMailMessage();
        // mail.setTo(user.getEmail());
        mail.setTo("flylivedrive@gmail.com");
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Verifikacija naloga");

        String salt = BCrypt.gensalt();
        String hash = BCrypt.hashpw(user.getUsername(), salt);

        String path = "http://localhost:8440/users/verify/" + user.getUsername() + "/" + processId;

        runtimeService.setVariable(processId, "hashedValue", hash); // sacuvaj za kasniju proveru

        // potreban jednokratni hash
        mail.setText("Zdravo " + user.getFirstName() + ",\n\n molimo vas da kliknete na sledeći link kako bi verifikovali svoj profil: "+ path);

        javaMailSender.send(mail);
    }

//    public List<Reviewer> findAllByMagazineScienceFields(List<ScienceField> scienceFields){
//        List<Reviewer> reviewers = findAllReviewers();
//        List<Reviewer> reviewerList = new ArrayList<>();
//
//        for(ScienceField field: scienceFields){
//            for(Reviewer reviewer: reviewers){
//                if(reviewerList.contains(reviewer)) continue;
//                for(ScienceField scienceField: reviewer.getScienceFields()){
//                    if(field.getName().equals(scienceField.getName())){
//                        reviewerList.add(reviewer);
//                        break;
//                    }
//                }
//            }
//        }
//        return reviewerList;
//    }

    public List<User> findAllByMagazineScienceFields(List<ScienceField> scienceFields, String type){
        List<User> users = new ArrayList<>();
        if(type.equals("REVIEWER")){
            users = findAllReviewers();
        }else{
            users = findAllEditors();
        }

        List<User> userList = new ArrayList<>();
        for(ScienceField field: scienceFields){
            for(User user: users){
                if(userList.contains(user)) continue;
                for(ScienceField scienceField: user.getScienceFields()){
                    if(field.getName().equals(scienceField.getName())){
                        userList.add(user);
                        break;
                    }
                }
            }
        }
        return userList;
    }

    public List<User> findAllReviewers(){
        List<User> reviewers = new ArrayList<>();
        List<User> users = userRepo.findAll();
        for(User user: users){
            if(user instanceof Reviewer){
                reviewers.add((Reviewer) user);
            }
        }
        return reviewers;
    }

    public List<User> findAllEditors(){
        List<User> editors = new ArrayList<>();
        List<User> users = userRepo.findAll();
        for(User user: users){
            if(user instanceof Editor){
                editors.add((Editor) user);
            }
        }
        return editors;
    }

    // * * * UTILS * * *

    private boolean namesValid(String text) {

        if(text.isEmpty()) {
            return false;
        }
        for(Character c : text.toCharArray()) {
            if (Character.isWhitespace(c) || !Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    // min 8 karaktera
    private boolean usernameValid(String text) {

        if(text.isEmpty()) {
            return false;
        }
        if(text.contains(";") || text.contains(">") || text.contains("<")) {
            return false;
        }
        for(Character c : text.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean passValid(String pass) {
        if(pass.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
        Matcher matcher = pattern.matcher(pass);

        return matcher.matches();
    }

    private boolean mailValid(String mail) {
        if(mail.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher matcher = pattern.matcher(mail);

        return matcher.matches();
    }
}
