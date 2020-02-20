package com.upp.naucnacentrala.repository.jpa;

import com.upp.naucnacentrala.model.SciencePaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SciencePaperRepository extends JpaRepository<SciencePaper, Long> {

    SciencePaper findOneById(long id);
}
