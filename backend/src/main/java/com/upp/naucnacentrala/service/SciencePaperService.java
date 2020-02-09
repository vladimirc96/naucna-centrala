package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Coauthor;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.ScienceField;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.repository.SciencePaperRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SciencePaperService {


    @Autowired
    SciencePaperRepository sciencePaperRepository;

    @Autowired
    private ScienceFieldService scienceFieldService;

    @Autowired
    private CoauthorService coauthorService;

    public SciencePaper save(SciencePaper sciencePaper){
        return sciencePaperRepository.save(sciencePaper);
    }

    public void remove(SciencePaper sciencePaper){
        sciencePaperRepository.delete(sciencePaper);
    }

    public SciencePaper findOneById(Long id){
        return sciencePaperRepository.findOneById(id);
    }

    public SciencePaper create(SciencePaper sciencePaper,List<FormSubmissionDto> sciencePaperData, ArrayList<Coauthor> coauthorList) {
        for(FormSubmissionDto dto: sciencePaperData){
            if(dto.getFieldId().equals("naslov_rada")){
                sciencePaper.setTitle(dto.getFieldValue());
            }else if(dto.getFieldId().equals("apstrakt")){
                sciencePaper.setPaperAbstract(dto.getFieldValue());
            }else if(dto.getFieldId().equals("kljucni_pojam")){
                sciencePaper.setKeyTerm(dto.getFieldValue());
            }else if(dto.getFieldId().equals("naucna_oblast")){
                ScienceField scienceField = scienceFieldService.findOneByName(dto.getFieldValue());
                sciencePaper.setScienceField(scienceField);
            }
        }
        for(Coauthor coauthor: coauthorList){
            Coauthor coauthorTemp = coauthorService.findOneById(coauthor.getId());
            sciencePaper.addCoauthor(coauthorTemp);
        }
        sciencePaper = sciencePaperRepository.save(sciencePaper);
        return sciencePaper;
    }


    public SciencePaper savePdf(MultipartFile file, SciencePaper sciencePaper){
        try {
            sciencePaper.setPdf(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sciencePaperRepository.save(sciencePaper);
    }




}
