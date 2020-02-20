package com.upp.naucnacentrala.repository.jpa;

import com.upp.naucnacentrala.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findOneByName(String name);

}
