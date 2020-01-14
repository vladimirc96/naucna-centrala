package com.upp.naucnacentrala.repository;

import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.ScienceField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    Magazine findOneById(Long id);

}
