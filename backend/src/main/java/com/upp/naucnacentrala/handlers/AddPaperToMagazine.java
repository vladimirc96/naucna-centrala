package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.SciencePaperService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddPaperToMagazine implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private SciencePaperService sciencePaperService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Magazine magazine = magazineService.findByName((String) delegateExecution.getVariable("magazineName"));
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) delegateExecution.getVariable("sciencePaperId"));
        magazine.addSciencePaper(sciencePaper);
        magazine = magazineService.save(magazine);
        System.out.println("********************************************");
        System.out.println("DODAVANJE RADA U CASOPIS " + magazine.getName());
        System.out.println("Rad " + sciencePaper.getTitle() + " dodat u casopis " +sciencePaper.getMagazine());
        System.out.println("********************************************");
    }

}
