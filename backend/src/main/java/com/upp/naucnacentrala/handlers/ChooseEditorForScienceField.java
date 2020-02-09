package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.SciencePaperService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ChooseEditorForScienceField implements JavaDelegate {

    @Autowired
    private SciencePaperService sciencePaperService;

    @Autowired
    private UserService userService;

    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) delegateExecution.getVariable("sciencePaperId"));
        Magazine magazine = magazineService.findByName((String) delegateExecution.getVariable("magazineName"));
        List<Editor> editorList = magazine.getScienceFieldEditors();
        List<User> list = new ArrayList<>();
        for(Editor editor: editorList){
            for(ScienceField scienceField: editor.getScienceFields()){
                if(scienceField.getName().equals(sciencePaper.getScienceField())){
                    list.add(editor);
                }
            }
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size() + 1);
        if(!list.isEmpty()){
            System.out.println("************************************************");
            System.out.println("Izabrani urednik naucne oblasti: " + list.get(randomNum).getUsername());
            System.out.println("************************************************");
            delegateExecution.setVariable("scienceFieldEditor", list.get(randomNum).getUsername());
        }else{
            delegateExecution.setVariable("scienceFieldEditor", null);
        }
    }
}
