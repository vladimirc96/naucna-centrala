package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.service.SciencePaperService;
import com.upp.naucnacentrala.service.elasticsearch.ReviewerESService;
import com.upp.naucnacentrala.service.elasticsearch.SciencePaperESService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AddDOI implements JavaDelegate {

    @Autowired
    private SciencePaperService sciencePaperService;

    @Autowired
    private SciencePaperESService sciencePaperESService;

    @Autowired
    private ReviewerESService reviewerESService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) delegateExecution.getVariable("sciencePaperId"));
        List<Coauthor> coauthorList = new ArrayList<>();
        for(Coauthor coauthor: sciencePaper.getCoauthors()){
            coauthorList.add(coauthor);
        }

        // sacuvaj u elastic-u rad
        SciencePaperES sciencePaperES = new SciencePaperES();
        sciencePaperES.setCoauthors(coauthorList);
        sciencePaperES.setId(sciencePaper.getId().toString());
        sciencePaperES.setKeyTerms(sciencePaper.getKeyTerm());
        sciencePaperES.setTitle(sciencePaper.getTitle());
        sciencePaperES.setPaperAbastract(sciencePaper.getPaperAbstract());
        sciencePaperES.setScienceField(sciencePaper.getScienceField().getName());
        sciencePaperES.setMagazineName(sciencePaper.getMagazine().getName());
        sciencePaperES.setFilePath(sciencePaperService.getPath(sciencePaper.getId()));
        Random rand = new Random();
        int prefix = rand.nextInt(1000) + 1;
        int suffix = rand.nextInt(1000) + 1;
        sciencePaperES.setDoi("10." + prefix + "/" + suffix);

        sciencePaperES = sciencePaperESService.save(sciencePaperES);

        // sacuvaj recenzente
        Magazine magazine = sciencePaper.getMagazine();
        List<ReviewerES> reviewerESList = new ArrayList<>();
        for(Reviewer reviewer: magazine.getReviewers()){
            ReviewerES reviewerES = new ReviewerES();
            reviewerES.setScienceFields(reviewer.getScienceFields());
            reviewerES.setFirstName(reviewer.getFirstName());
            reviewerES.setLastName(reviewer.getLastName());
            reviewerES.setEmail(reviewer.getEmail());
            reviewerES.setId(reviewer.getUsername());
            reviewerES.getSciencePapers().add(sciencePaperES);
            reviewerES = reviewerESService.save(reviewerES);
        }

    }
}
