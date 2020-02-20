package com.upp.naucnacentrala.repository.elasticsearch;

import com.upp.naucnacentrala.model.ReviewerES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReviewerESRepository extends ElasticsearchRepository<ReviewerES, String> {

    ReviewerES findOneById(String id);

}
