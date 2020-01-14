package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Magazine;
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

    }
}
