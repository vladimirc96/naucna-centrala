package com.upp.naucnacentrala.repository.elasticsearch;

import com.upp.naucnacentrala.model.SciencePaperES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SciencePaperESRepository extends ElasticsearchRepository<SciencePaperES, String> {

    SciencePaperES findOneById(String id);

}
