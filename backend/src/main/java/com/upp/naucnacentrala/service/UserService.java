package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.User;
import com.upp.naucnacentrala.repository.UserRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    private UserRepository userRepo;

    public User save(User user){
        return userRepo.save(user);
    }

    public User save(User user, List<FormSubmissionDto> registrationData){
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
                        user.setReviewer(true);
                    }
                }else{
                    user.setReviewer(false);
                }
            }

        }
        user.setActive(false);
        return userRepo.save(user);
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

    public List<User> findAll(){
        return userRepo.findAll();
    }

    public void remove(User user){
        userRepo.delete(user);
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
        mail.setText("Zdravo " + user.getUsername() + ",\n\n molimo vas da kliknete na sledeći link kako bi verifikovali svoj profil: "+ path);

        javaMailSender.send(mail);
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
