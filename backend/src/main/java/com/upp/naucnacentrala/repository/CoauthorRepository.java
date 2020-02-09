package com.upp.naucnacentrala.repository;

import com.upp.naucnacentrala.model.Coauthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoauthorRepository extends JpaRepository<Coauthor, Long> {

    Coauthor findOneById(Long id);
}
