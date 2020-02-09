package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.model.Author;
import com.upp.naucnacentrala.model.Editor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment environment;


    public void sendNotification(Author author, Editor chiefEditor){
        SimpleMailMessage mailAuthor = new SimpleMailMessage();
        mailAuthor.setTo("flylivedrive@gmail.com");
        mailAuthor.setFrom(environment.getProperty("spring.mail.username"));
        mailAuthor.setSubject("Nov rad");
        mailAuthor.setText("Zdravo " + author.getFirstName() + ",\n\n uspešno ste prijavili rad.");

        SimpleMailMessage mailEditor = new SimpleMailMessage();
        mailEditor.setTo("flylivedrive@gmail.com");
        mailEditor.setFrom(environment.getProperty("spring.mail.username"));
        mailEditor.setSubject("Nov rad");
        mailEditor.setText("Zdravo " + chiefEditor.getFirstName() + ",\n\n obaveštavamo Vas da je u sistemu prijavljen nov rad za časopis "
                + chiefEditor.getMagazine().getName() + ".");


        javaMailSender.send(mailAuthor);
        javaMailSender.send(mailEditor);
    }


}
