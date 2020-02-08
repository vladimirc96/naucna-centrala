package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Coauthor;
import com.upp.naucnacentrala.repository.CoauthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoauthorService {

    @Autowired
    private CoauthorRepository coauthorRepo;

    public Coauthor create(List<FormSubmissionDto> coauthorData){
        Coauthor coauthor = new Coauthor();
        for(FormSubmissionDto dto: coauthorData){
            if(dto.getFieldId().equals("ime_koautora")){
                coauthor.setFristName(dto.getFieldValue());
            }else if(dto.getFieldId().equals("prezime_koautora")){
                coauthor.setLastName(dto.getFieldValue());
            }else if(dto.getFieldId().equals("email_koautora")){
                coauthor.setEmail(dto.getFieldValue());
            }else if(dto.getFieldId().equals("adresa_koautora")){
                coauthor.setAddress(dto.getFieldValue());
            }
        }
        return coauthorRepo.save(coauthor);
    }

}
