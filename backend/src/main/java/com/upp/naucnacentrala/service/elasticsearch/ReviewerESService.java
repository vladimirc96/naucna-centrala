package com.upp.naucnacentrala.service.elasticsearch;

import com.upp.naucnacentrala.model.ReviewerES;
import com.upp.naucnacentrala.repository.elasticsearch.ReviewerESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewerESService {

    @Autowired
    private ReviewerESRepository reviewerESRepo;

    public ReviewerES save(ReviewerES reviewerES){
        return reviewerESRepo.save(reviewerES);
    }

    public ReviewerES findOneById(String id){
        return reviewerESRepo.findOneById(id);
    }

}
