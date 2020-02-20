package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.model.Role;
import com.upp.naucnacentrala.repository.jpa.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepo;

    public Role findOneByName(String name){
        return roleRepo.findOneByName(name);
    }

    public List<Role> findAll(){
        return roleRepo.findAll();
    }

    public void remove(Role role){
        roleRepo.delete(role);
    }

    public Role save(Role role){
        return roleRepo.save(role);
    }
}
