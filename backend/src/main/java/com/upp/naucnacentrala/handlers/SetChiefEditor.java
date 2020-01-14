package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.model.Editor;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.service.MagazineService;
import com.upp.naucnacentrala.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetChiefEditor implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = (Long) delegateExecution.getVariable("magazineId");
        Magazine magazine = magazineService.findOneById(id);
        String username = (String) delegateExecution.getVariable("userId");
        Editor editor = (Editor) userService.findOneByUsername(username);

        magazine.setChiefEditor(editor);
        magazine = magazineService.save(magazine);
    }

}
