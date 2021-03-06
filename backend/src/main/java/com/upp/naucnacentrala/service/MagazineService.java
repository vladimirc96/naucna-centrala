package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.dto.MagazineDTO;
import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.model.enums.BillingType;
import com.upp.naucnacentrala.repository.jpa.MagazineRepository;
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

    public MagazineDTO findOneDto(Long id){
        Magazine magazine = magazineRepo.findOneById(id);
        if(magazine.getSellerId() != null){
            return new MagazineDTO(magazine.getId(), magazine.getName(), magazine.getIssn(), magazine.getScienceFields(),
                    magazine.getChiefEditor(), magazine.isRegistered(), magazine.getSellerId(), magazine.getSciencePapers());
        }else{
            return new MagazineDTO(magazine.getId(), magazine.getName(), magazine.getIssn(), magazine.getScienceFields(),
                    magazine.getChiefEditor(), magazine.isRegistered(), new Long(0), magazine.getSciencePapers());
        }
    }

    public Magazine save(Magazine magazine){
        return magazineRepo.save(magazine);
    }

    public List<MagazineDTO> findAll(){
        List<MagazineDTO> magazines = new ArrayList<>();
        for(Magazine magazine: magazineRepo.findAll()){
            if(magazine.getSellerId() != null){
                magazines.add(new MagazineDTO(magazine.getId(), magazine.getName(), magazine.getIssn(),
                        magazine.getScienceFields(), magazine.getChiefEditor(), magazine.isRegistered(), magazine.getSellerId(), magazine.getSciencePapers()));
            }else{
                magazines.add(new MagazineDTO(magazine.getId(), magazine.getName(), magazine.getIssn(),
                        magazine.getScienceFields(), magazine.getChiefEditor(), magazine.isRegistered(), new Long(0), magazine.getSciencePapers()));

            }
        }
        return magazines;
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

        for(FormSubmissionDto dto: editorialBoard){
            if(dto.getFieldId().equals("recenzenti")){
                Reviewer reviewer = (Reviewer) userService.findOneByUsername(dto.getFieldValue());
                reviewers.add(reviewer);
            }
            if(dto.getFieldId().equals("urednici")){
                Editor editor = (Editor) userService.findOneByUsername(dto.getFieldValue());
                magazine.addEditor(editor);
            }
        }
        magazine.setReviewers(reviewers);
        magazine = magazineRepo.save(magazine);
        return magazine;
    }

    public void remove(Magazine magazine){
        magazineRepo.delete(magazine);
    }

    public Magazine magazineCorrection(List<FormSubmissionDto> magazineCorrectionData, Magazine magazine){
        List<Reviewer> reviewers = new ArrayList<>();
        List<ScienceField> fields = new ArrayList<>();

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
            }else if(dto.getFieldId().equals("naucne_oblasti_ispravka")){
                if(!magazine.getScienceFields().isEmpty()) {
                    magazine.getScienceFields().clear();
                }
                ScienceField field = scienceFieldService.findOneByName(dto.getFieldValue());
                fields.add(field);
            }else if(dto.getFieldId().equals("urednici_ispravka")){
                if(!magazine.getScienceFieldEditors().isEmpty()){
                    magazine.clearEditors();
                }
                Editor editor = (Editor) userService.findOneByUsername(dto.getFieldValue());
                magazine.addEditor(editor);
            }else if(dto.getFieldId().equals("recenzenti_ispravka")){
                if(!magazine.getReviewers().isEmpty()){
                    magazine.getReviewers().clear();
                }
                Reviewer reviewer = (Reviewer) userService.findOneByUsername(dto.getFieldValue());
                reviewer.getMagazines().add(magazine);
                reviewers.add(reviewer);
            }
        }

        if(!reviewers.isEmpty()){
            magazine.setReviewers(reviewers);
        }

        if(!fields.isEmpty()){
            magazine.setScienceFields(fields);
        }

        magazine = magazineRepo.save(magazine);
        return magazine;
    }

    public List<MagazineDTO> findAllByChiefEditor(String username){
        List<MagazineDTO> magazines = new ArrayList<>();
        for(Magazine magazine: magazineRepo.findAllByChiefEditor(username)){
            if(magazine.getSellerId() != null){
                magazines.add(new MagazineDTO(magazine.getId(), magazine.getName(), magazine.getIssn(),
                        magazine.getScienceFields(), magazine.getChiefEditor(), magazine.isRegistered(), magazine.getSellerId(), magazine.getSciencePapers()));
            }else{
                magazines.add(new MagazineDTO(magazine.getId(), magazine.getName(), magazine.getIssn(),
                        magazine.getScienceFields(), magazine.getChiefEditor(), magazine.isRegistered(), new Long(0), magazine.getSciencePapers()));
            }
        }
        return magazines;
    }

    public Magazine findByName(String name){
        return magazineRepo.findByName(name);
    }

    public List<Magazine> getAll(){
        return magazineRepo.findAll();
    }


    public Magazine findBySellerId(long sellerID) {
        return magazineRepo.findBySellerId(sellerID);
    }
}
