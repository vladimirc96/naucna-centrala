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
        mailAuthor.setText("Zdravo " + author.getFirstName() + ",\n\nuspešno ste prijavili rad.");

        SimpleMailMessage mailEditor = new SimpleMailMessage();
        mailEditor.setTo("flylivedrive@gmail.com");
        mailEditor.setFrom(environment.getProperty("spring.mail.username"));
        mailEditor.setSubject("Nov rad");
        mailEditor.setText("Zdravo " + chiefEditor.getFirstName() + ",\n\nobaveštavamo Vas da je u sistemu prijavljen nov rad za časopis.");


        javaMailSender.send(mailAuthor);
        javaMailSender.send(mailEditor);
    }


    public void paperNotAppropriate(Author author) {
        SimpleMailMessage mailAuthor = new SimpleMailMessage();
        mailAuthor.setTo("flylivedrive@gmail.com");
        mailAuthor.setFrom(environment.getProperty("spring.mail.username"));
        mailAuthor.setSubject("Neprikladan rad");
        mailAuthor.setText("Zdravo " + author.getFirstName() + ",\n\nobaveštavamo Vas da rad koji ste prijavili nije tematski priklada za časopis.");
        javaMailSender.send(mailAuthor);
    }

    public void correctionNotification(Author author) {
        SimpleMailMessage mailAuthor = new SimpleMailMessage();
        mailAuthor.setTo("flylivedrive@gmail.com");
        mailAuthor.setFrom(environment.getProperty("spring.mail.username"));
        mailAuthor.setSubject("Ispravka rada");
        mailAuthor.setText("Zdravo " + author.getFirstName() + ",\n\nobaveštavamo Vas da rad koji ste priložili zahteva ispravke i potrebno je priložiti nov PDF. Imate 24h da ispravite rad.");
        javaMailSender.send(mailAuthor);
    }

    public void rejectedForTechnicalReasons(Author author) {
        SimpleMailMessage mailAuthor = new SimpleMailMessage();
        mailAuthor.setTo("flylivedrive@gmail.com");
        mailAuthor.setFrom(environment.getProperty("spring.mail.username"));
        mailAuthor.setSubject("Rad odbijen");
        mailAuthor.setText("Zdravo " + author.getFirstName() + ",\n\nobaveštavamo Vas da je rad koji ste priložili odbijen iz tehnčkih razloga.");
        javaMailSender.send(mailAuthor);
    }

    public void notifyScienceFieldEditor(Editor editor) {
        SimpleMailMessage mailEditor = new SimpleMailMessage();
        mailEditor.setTo("flylivedrive@gmail.com");
        mailEditor.setFrom(environment.getProperty("spring.mail.username"));
        mailEditor.setSubject("Nov rad");
        mailEditor.setText("Zdravo " + editor.getFirstName() + ",\n\nobaveštavamo Vas da je u sistemu prijavljen nov rad.");
        javaMailSender.send(mailEditor);
    }

    public void notifyAboutRefusal(Author author) {
        SimpleMailMessage mailAuthor = new SimpleMailMessage();
        mailAuthor.setTo("flylivedrive@gmail.com");
        mailAuthor.setFrom(environment.getProperty("spring.mail.username"));
        mailAuthor.setSubject("Odluka o prihvaćanju rada");
        mailAuthor.setText("Zdravo " + author.getFirstName() + ",\n\nnažalost Vas obaveštavamo da je rad koji ste priložili odbijen od strane glavnog urednika.");
        javaMailSender.send(mailAuthor);
    }

    public void notifyAboutAcceptance(Author author) {
        SimpleMailMessage mailAuthor = new SimpleMailMessage();
        mailAuthor.setTo("flylivedrive@gmail.com");
        mailAuthor.setFrom(environment.getProperty("spring.mail.username"));
        mailAuthor.setSubject("Odluka o prihvaćanju rada");
        mailAuthor.setText("Zdravo " + author.getFirstName() + ",\n\nobaveštavamo Vas da je rad koji ste priložili prihvaćen od strane glavnog urednika.");
        javaMailSender.send(mailAuthor);
    }
}
