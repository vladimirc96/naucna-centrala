package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Coauthor;
import com.upp.naucnacentrala.model.SciencePaper;
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

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<FormSubmissionDto> sciencePaperData = (List<FormSubmissionDto>) delegateExecution.getVariable("sciencePaperData");
        ArrayList<Coauthor> coauthors = (ArrayList<Coauthor>) delegateExecution.getVariable("coauthorList");
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) delegateExecution.getVariable("sciencePaperId"));
        sciencePaper = sciencePaperService.create(sciencePaperData, coauthors);
    }
}
