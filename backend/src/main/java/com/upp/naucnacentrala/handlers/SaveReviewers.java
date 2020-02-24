package com.upp.naucnacentrala.handlers;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import org.aspectj.apache.bcel.generic.FieldOrMethod;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaveReviewers implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<FormSubmissionDto> reviewersData = (List<FormSubmissionDto>) delegateExecution.getVariable("reviewersData");
        List<String> reviewerList = new ArrayList<>();
        for(FormSubmissionDto dto: reviewersData){
            reviewerList.add(dto.getFieldValue());
        }
        // ovde sacuvati recenzenta za casopis
        delegateExecution.setVariable("reviewerList", reviewerList);
    }
}
