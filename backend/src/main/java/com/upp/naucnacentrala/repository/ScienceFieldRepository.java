package com.upp.naucnacentrala.repository;

import com.upp.naucnacentrala.model.ScienceField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScienceFieldRepository extends JpaRepository<ScienceField, Long> {

    ScienceField findOneByName(String name);

}
