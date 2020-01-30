package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.repository.SciencePaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SciencePaperService {

    @Autowired
    SciencePaperRepository sciencePaperRepository;

    public SciencePaper findOneById(Long id){
        return sciencePaperRepository.findOneById(id);
    }
}
