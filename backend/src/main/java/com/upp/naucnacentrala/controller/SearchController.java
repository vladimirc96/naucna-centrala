package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.client.GoogleClient;
import com.upp.naucnacentrala.dto.*;
import com.upp.naucnacentrala.model.Author;
import com.upp.naucnacentrala.model.Location;
import com.upp.naucnacentrala.search.ResultRetriever;
import com.upp.naucnacentrala.search.SearchType;
import com.upp.naucnacentrala.service.UserService;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private ResultRetriever resultRetriever;

    @Autowired
    private GoogleClient googleClient;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/match", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<SearchSciencePaperDTO>> searchTermQuery(@RequestBody SimpleQueryDTO simpleQueryDTO){
        QueryBuilder queryBuilder = com.upp.naucnacentrala.search.QueryBuilder.buildSimpleQuery(simpleQueryDTO,SearchType.REGULAR);
        List<SearchSciencePaperDTO> results = resultRetriever.getSciencePaperResults(queryBuilder);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping(value = "/boolean", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<SearchSciencePaperDTO>> searchBooleanQuery(@RequestBody BooleanQueryDTO booleanQueryDTO){
        BoolQueryBuilder booleanQueryBuilder = com.upp.naucnacentrala.search.QueryBuilder.buildBooleanQuery(booleanQueryDTO);
        List<SearchSciencePaperDTO> results = resultRetriever.getSciencePaperResults(booleanQueryBuilder);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping(value = "/distance/{username}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<ReviewerDTO>> searchBooleanQuery(@PathVariable("username") String username){
        Author author = (Author) userService.findOneByUsername(username);
        Location location = googleClient.getCoordinates(author.getCity());
        BoolQueryBuilder booleanQueryBuilder = com.upp.naucnacentrala.search.QueryBuilder.buildGeoQuery(location);
        List<ReviewerDTO> results = resultRetriever.getReviewerResults(booleanQueryBuilder);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }



}
