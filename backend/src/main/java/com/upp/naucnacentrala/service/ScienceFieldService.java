package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.model.ScienceField;
import com.upp.naucnacentrala.repository.ScienceFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScienceFieldService {

    @Autowired
    private ScienceFieldRepository scienceFieldRepository;

    public ScienceField findOneByName(String name){
        return scienceFieldRepository.findOneByName(name);
    }

}
