package com.upp.naucnacentrala.service.elasticsearch;

import com.upp.naucnacentrala.model.SciencePaperES;
import com.upp.naucnacentrala.repository.elasticsearch.SciencePaperESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SciencePaperESService {

    @Autowired
    private SciencePaperESRepository sciencePaperESRepo;


    public SciencePaperES save(SciencePaperES sciencePaperES){
        return sciencePaperESRepo.save(sciencePaperES);
    }


    public SciencePaperES findOneById(String id) {
        return sciencePaperESRepo.findOneById(id);
    }
}
