package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Author;
import com.upp.naucnacentrala.model.Coauthor;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.SciencePaperService;
import com.upp.naucnacentrala.service.UserService;
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

    @Autowired
    private UserService userService;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Author author = (Author) userService.findOneByUsername((String) delegateExecution.getVariable("authorId"));
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) delegateExecution.getVariable("sciencePaperId"));
        List<FormSubmissionDto> sciencePaperData = (List<FormSubmissionDto>) delegateExecution.getVariable("sciencePaperData");
        ArrayList<Coauthor> coauthors = (ArrayList<Coauthor>) delegateExecution.getVariable("coauthorList");
        sciencePaper = sciencePaperService.create(sciencePaper, sciencePaperData, coauthors, author);
    }
}
