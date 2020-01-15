package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.repository.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MagazineService {

    @Autowired
    private MagazineRepository magazineRepo;

    @Autowired
    private ScienceFieldService scienceFieldService;

    @Autowired
    private UserService userService;

    public Magazine findOneById(Long id){
        return magazineRepo.findOneById(id);
    }

    public Magazine save(Magazine magazine){
        return magazineRepo.save(magazine);
    }

    public Magazine save(List<FormSubmissionDto> magazineData){
        Magazine magazine = new Magazine();
        List<ScienceField> fields = new ArrayList<>();
        for(FormSubmissionDto dto : magazineData){

            if(dto.getFieldId().equals("naziv")){
                magazine.setName(dto.getFieldValue());
            }else if(dto.getFieldId().equals("issn")){
                magazine.setIssn(dto.getFieldValue());
            }else if(dto.getFieldId().equals("nacin_naplacivanja")){
                if(dto.getFieldValue().equals("autorima")){
                    magazine.setBillingType(BillingType.AUTHORS);
                }else{
                    magazine.setBillingType(BillingType.READERS);
                }
            }else if(dto.getFieldId().equals("naucne_oblasti")){
                ScienceField field = scienceFieldService.findOneByName(dto.getFieldValue());
                fields.add(field);
            }

        }
        magazine.setScienceFields(fields);
        magazine = magazineRepo.save(magazine);
        return magazine;
    }

    public Magazine saveEditorialBoard(List<FormSubmissionDto> editorialBoard, Magazine magazine){
        List<Reviewer> reviewers = new ArrayList<>();
        List<Editor> editors = new ArrayList<>();

        for(FormSubmissionDto dto: editorialBoard){
            if(dto.getFieldId().equals("recenzenti")){
                Reviewer reviewer = (Reviewer) userService.findOneByUsername(dto.getFieldValue());
                reviewers.add(reviewer);
            }
            if(dto.getFieldId().equals("urednici")){
                Editor editor = (Editor) userService.findOneByUsername(dto.getFieldValue());
                editor.setMagazine(magazine);
                editors.add(editor);
            }
        }
        magazine.setReviewers(reviewers);
        magazine.setScienceFieldEditors(editors);
        magazine = magazineRepo.save(magazine);
        return magazine;
    }

    public List<Magazine> findAll(){
        return magazineRepo.findAll();
    }

    public void remove(Magazine magazine){
        magazineRepo.delete(magazine);
    }

    public Magazine magazineCorrection(List<FormSubmissionDto> magazineCorrectionData, Magazine magazine){
        for(FormSubmissionDto dto: magazineCorrectionData){
            if(dto.getFieldId().equals("naziv_stari")){
                magazine.setName(dto.getFieldValue());
            }else if(dto.getFieldId().equals("issn_stari")){
                magazine.setIssn(dto.getFieldValue());
            }else if(dto.getFieldId().equals("nacin_naplacivanja_stari")){
                if(dto.getFieldValue().equals("Autorima")){
                    magazine.setBillingType(BillingType.AUTHORS);
                }else{
                    magazine.setBillingType(BillingType.READERS);
                }
            }
        }

        magazine = magazineRepo.save(magazine);
        return magazine;
    }


}
