package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.model.Membership;
import com.upp.naucnacentrala.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.lang.reflect.Member;
import java.util.List;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepo;

    public List<Membership> findAll(){
        return membershipRepo.findAll();
    }


}
