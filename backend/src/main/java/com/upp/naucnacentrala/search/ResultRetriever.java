package com.upp.naucnacentrala.search;

import com.upp.naucnacentrala.dto.SearchSciencePaperDTO;
import com.upp.naucnacentrala.model.SciencePaperES;
import com.upp.naucnacentrala.repository.elasticsearch.ReviewerESRepository;
import com.upp.naucnacentrala.repository.elasticsearch.SciencePaperESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultRetriever {

    @Autowired
    private SciencePaperESRepository sciencePaperESRepository;

    @Autowired
    private ReviewerESRepository reviewerESRepository;

    public List<SearchSciencePaperDTO> getSciencePaperResults(org.elasticsearch.index.query.QueryBuilder query){
        if(query == null){
            return null;
        }

        List<SearchSciencePaperDTO> results = new ArrayList<>();
        for(SciencePaperES sciencePaperES: sciencePaperESRepository.search(query)){
            results.add(new SearchSciencePaperDTO(sciencePaperES.getId(), sciencePaperES.getTitle()));
        }
        return results;
    }


}