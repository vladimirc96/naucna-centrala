package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Coauthor;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.SciencePaperService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaveSciencePaper implements JavaDelegate {

    @Autowired
    private SciencePaperService sciencePaperService;

    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) delegateExecution.getVariable("sciencePaperId"));
        List<FormSubmissionDto> sciencePaperData = (List<FormSubmissionDto>) delegateExecution.getVariable("sciencePaperData");
        List<FormSubmissionDto> magazineName = (List<FormSubmissionDto>) delegateExecution.getVariable("magazineName");
        ArrayList<Coauthor> coauthors = (ArrayList<Coauthor>) delegateExecution.getVariable("coauthorList");
        Magazine magazine = magazineService.findByName(magazineName.iterator().next().getFieldValue());

        sciencePaper = sciencePaperService.create(sciencePaper, sciencePaperData, coauthors);
        // uvrstavanje u magazin se radi na kraju
        //magazine.addSciencePaper(sciencePaper);
        //magazine = magazineService.save(magazine);
    }
}
