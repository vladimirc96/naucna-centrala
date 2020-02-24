package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.client.GoogleClient;
import com.upp.naucnacentrala.dto.*;
import com.upp.naucnacentrala.model.Author;
import com.upp.naucnacentrala.model.Location;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.search.ResultRetriever;
import com.upp.naucnacentrala.search.SearchType;
import com.upp.naucnacentrala.service.SciencePaperService;
import com.upp.naucnacentrala.service.UserService;
import com.upp.naucnacentrala.service.elasticsearch.SciencePaperESService;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    private ResultRetriever resultRetriever;

    @Autowired
    private GoogleClient googleClient;

    @Autowired
    private UserService userService;

    @Autowired
    private SciencePaperService sciencePaperService;

    @Autowired
    private SciencePaperESService sciencePaperESService;

    @PostMapping(value = "/match", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<SearchSciencePaperDTO>> searchTermQuery(@RequestBody SimpleQueryDTO simpleQueryDTO){
        SearchQuery searchQuery = com.upp.naucnacentrala.search.QueryBuilder.buildSimpleQuery(simpleQueryDTO);
        List<SearchSciencePaperDTO> results = resultRetriever.getSciencePaperResults(searchQuery);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping(value = "/boolean", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<SearchSciencePaperDTO>> searchBooleanQuery(@RequestBody BooleanQueryDTO booleanQueryDTO){
        SearchQuery booleanQueryBuilder = com.upp.naucnacentrala.search.QueryBuilder.buildBooleanQuery(booleanQueryDTO);
        List<SearchSciencePaperDTO> results = resultRetriever.getSciencePaperResults(booleanQueryBuilder);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(value = "/distance/{taskId}", produces = "application/json")
    public ResponseEntity<List<ReviewerDTO>> searchDistanceQuery(@PathVariable("taskId") String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        Author author = (Author) userService.findOneByUsername((String) runtimeService.getVariable(pi.getId(), "authorId"));
        Location location = googleClient.getCoordinates(author.getCity());
        BoolQueryBuilder booleanQueryBuilder = com.upp.naucnacentrala.search.QueryBuilder.buildGeoQuery(location);
        List<ReviewerDTO> results = resultRetriever.getReviewerResults(booleanQueryBuilder);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }


    @GetMapping(value = "/more-like-this/{taskId}", produces = "application/json")
    public ResponseEntity<List<ReviewerDTO>> searchMoreLikeThisQuery(@PathVariable("taskId") String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) runtimeService.getVariable(pi.getId(), "sciencePaperId"));
        String text = null;
        try {
            text = sciencePaperESService.parsePDF(sciencePaper);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SearchQuery searchQuery = com.upp.naucnacentrala.search.QueryBuilder.buildMoreLikeThisQuery(text);
        List<SearchSciencePaperDTO> results = resultRetriever.getSciencePaperResults(searchQuery);
        org.elasticsearch.index.query.QueryBuilder reviewerQuery = com.upp.naucnacentrala.search.QueryBuilder.buildGetReviewersQuery(results);
        List<ReviewerDTO> finalres = resultRetriever.getReviewerResults(reviewerQuery);
        return new ResponseEntity<>(finalres, HttpStatus.OK);
    }


    @GetMapping(value = "/more-like-this-test/{sciencePaperId}", produces = "application/json")
    public ResponseEntity<List<ReviewerDTO>> searchMoreLikeThisQueryTest(@PathVariable("sciencePaperId") String id){
        SciencePaper sciencePaper = sciencePaperService.findOneById(Long.parseLong(id));
        String text = null;
        System.out.println("**********************************");
        System.out.println("PARSIRANJE: PAPER: " + sciencePaper.toString());
        try {
            text = sciencePaperESService.parsePDF(sciencePaper);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SearchQuery searchQuery = com.upp.naucnacentrala.search.QueryBuilder.buildMoreLikeThisQuery(text);
        List<SearchSciencePaperDTO> results = resultRetriever.getSciencePaperResults(searchQuery);
        org.elasticsearch.index.query.QueryBuilder reviewerQuery = com.upp.naucnacentrala.search.QueryBuilder.buildGetReviewersQuery(results);
        List<ReviewerDTO> finalres = resultRetriever.getReviewerResults(reviewerQuery);
        System.out.println("**********************************");
        return new ResponseEntity<>(finalres, HttpStatus.OK);
    }

}
