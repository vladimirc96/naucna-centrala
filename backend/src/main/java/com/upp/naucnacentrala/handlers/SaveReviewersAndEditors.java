package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Editor;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.Reviewer;
import com.upp.naucnacentrala.model.ScienceField;
import com.upp.naucnacentrala.service.MagazineService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaveReviewersAndEditors implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Long id = (Long) delegateExecution.getVariable("magazineId");
        List<FormSubmissionDto> editorialBoard = (List<FormSubmissionDto>) delegateExecution.getVariable("editorialBoardData");
        Magazine magazine = magazineService.findOneById(id);
        magazine = magazineService.saveEditorialBoard(editorialBoard, magazine);

        String oldScienceFields = "";
        String oldEditors = "";
        String oldReviewers = "";
        for(ScienceField scienceField: magazine.getScienceFields()){
            oldScienceFields = oldScienceFields + scienceField.getName() + ", ";
        }
        for(Editor editor: magazine.getScienceFieldEditors()){
            oldEditors = oldEditors + editor.getFirstName() + " " + editor.getLastName() + ", ";
        }
        for(Reviewer reviewer: magazine.getReviewers()){
            oldReviewers = oldReviewers + reviewer.getFirstName() + " " + reviewer.getLastName() + ", ";
        }

        oldScienceFields = oldScienceFields.substring(0, oldScienceFields.length()-2);
        oldEditors = oldEditors.substring(0, oldEditors.length()-2);
        oldReviewers = oldReviewers.substring(0, oldReviewers.length()-2);

        System.out.println("**********************************");

        System.out.println("NAUCNE OBLASTI: " + oldScienceFields);
        System.out.println("RECENZENTI: " + oldReviewers);
        System.out.println("UREDNICI: " + oldEditors);

        delegateExecution.setVariable("oldScienceFields", oldScienceFields);
        delegateExecution.setVariable("oldReviewers", oldReviewers);
        delegateExecution.setVariable("oldEditors", oldEditors);

        System.out.println("**********************************");

    }
}
