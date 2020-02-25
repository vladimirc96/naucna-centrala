package com.upp.naucnacentrala.search;

import com.upp.naucnacentrala.dto.ReviewerDTO;
import com.upp.naucnacentrala.dto.SearchSciencePaperDTO;
import com.upp.naucnacentrala.model.Reviewer;
import com.upp.naucnacentrala.model.ReviewerES;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.model.SciencePaperES;
import com.upp.naucnacentrala.model.enums.BillingType;
import com.upp.naucnacentrala.repository.elasticsearch.ReviewerESRepository;
import com.upp.naucnacentrala.repository.elasticsearch.SciencePaperESRepository;
import com.upp.naucnacentrala.service.SciencePaperService;
import com.upp.naucnacentrala.service.UserService;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Field;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultRetriever {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private SciencePaperESRepository sciencePaperESRepository;

    @Autowired
    private ReviewerESRepository reviewerESRepository;

    @Autowired
    private SciencePaperService sciencePaperService;

    @Autowired
    private UserService userService;

    public List<SearchSciencePaperDTO> getSciencePaperResults(SearchQuery query){
        if(query == null){
            return null;
        }
        if(query.getHighlightFields() == null){
            return getWithoutHighlights(query);
        }

        return getWithHighlights(query, query.getHighlightFields());
    }

    private List<SearchSciencePaperDTO> getWithHighlights(SearchQuery query, HighlightBuilder.Field[] fields){
        ResultMapper resultMapper = new ResultMapper();
        resultMapper.setFields(fields);
        Page<SciencePaperES> results = elasticsearchOperations.queryForPage(query, SciencePaperES.class, resultMapper);

        List<SearchSciencePaperDTO> searchSciencePaperDTOList = new ArrayList<>();
        if(results == null) return searchSciencePaperDTOList;
        for(SciencePaperES sciencePaperES: results){
            SciencePaper sciencePaper = sciencePaperService.findOneById(Long.parseLong(sciencePaperES.getId()));
            if(sciencePaper.getMagazine().getBillingType().equals(BillingType.AUTHORS)){
                searchSciencePaperDTOList.add(new SearchSciencePaperDTO(sciencePaperES.getId(), sciencePaperES.getTitle(), sciencePaper.getCurrency(),
                        sciencePaper.getPrice(), sciencePaperES.getHighlight(),
                        true,sciencePaper.getAuthor().getFirstName() + " " + sciencePaper.getAuthor().getLastName()));
            }else{
                searchSciencePaperDTOList.add(new SearchSciencePaperDTO(sciencePaperES.getId(), sciencePaperES.getTitle(), sciencePaper.getCurrency(),
                        sciencePaper.getPrice(), sciencePaperES.getHighlight(),
                        false, sciencePaper.getAuthor().getFirstName() + " " + sciencePaper.getAuthor().getLastName()));
            }
        }
        return searchSciencePaperDTOList;
    }

    private List<SearchSciencePaperDTO> getWithoutHighlights(SearchQuery searchQuery){
        List<SearchSciencePaperDTO> searchSciencePaperDTOList = new ArrayList<>();
        for(SciencePaperES sciencePaperES: sciencePaperESRepository.search(searchQuery)){
            SciencePaper sciencePaper = sciencePaperService.findOneById(Long.parseLong(sciencePaperES.getId()));
            if(sciencePaper.getMagazine().getBillingType().equals(BillingType.AUTHORS)){
                searchSciencePaperDTOList.add(new SearchSciencePaperDTO(sciencePaperES.getId(), sciencePaperES.getTitle(), sciencePaper.getCurrency(),
                        sciencePaper.getPrice(), sciencePaperES.getHighlight(),
                        true,sciencePaper.getAuthor().getFirstName() + " " + sciencePaper.getAuthor().getLastName()));
            }else{
                searchSciencePaperDTOList.add(new SearchSciencePaperDTO(sciencePaperES.getId(), sciencePaperES.getTitle(), sciencePaper.getCurrency(),
                        sciencePaper.getPrice(), sciencePaperES.getHighlight(),
                        false, sciencePaper.getAuthor().getFirstName() + " " + sciencePaper.getAuthor().getLastName()));
            }
        }
        return searchSciencePaperDTOList;
    }

    public List<ReviewerDTO> getReviewerResults(org.elasticsearch.index.query.QueryBuilder query){
        if(query == null){
            return null;
        }
        List<ReviewerDTO> results = new ArrayList<>();
        for(ReviewerES reviewerES: reviewerESRepository.search(query)){
            Reviewer reviewer = (Reviewer) userService.findOneByUsername(reviewerES.getId());
            results.add(new ReviewerDTO(reviewer.getFirstName() + " " + reviewer.getLastName()));
        }
        return results;
    }

}
