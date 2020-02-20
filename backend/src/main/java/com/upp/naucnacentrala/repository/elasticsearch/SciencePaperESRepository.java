package com.upp.naucnacentrala.repository.elasticsearch;

import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.model.SciencePaperES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

public interface SciencePaperESRepository extends ElasticsearchRepository<SciencePaperES, String> {

    SciencePaperES findOneById(String id);

}
