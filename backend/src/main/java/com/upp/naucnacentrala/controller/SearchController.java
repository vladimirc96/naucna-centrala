package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.dto.BooleanQueryDTO;
import com.upp.naucnacentrala.dto.SearchSciencePaperDTO;
import com.upp.naucnacentrala.dto.SimpleQueryDTO;
import com.upp.naucnacentrala.search.ResultRetriever;
import com.upp.naucnacentrala.search.SearchType;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private ResultRetriever resultRetriever;


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

}
