package com.upp.naucnacentrala.search;

import com.upp.naucnacentrala.dto.BooleanQueryDTO;
import com.upp.naucnacentrala.dto.SimpleQueryDTO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    public static  org.elasticsearch.index.query.QueryBuilder buildSimpleQuery(SimpleQueryDTO simpleQueryDTO, SearchType searchType){
        processSimpleQuery(simpleQueryDTO);
        org.elasticsearch.index.query.QueryBuilder retVal = null;
        if(simpleQueryDTO.getType().equals("phrase")){
            retVal = QueryBuilders.matchPhraseQuery(simpleQueryDTO.getField(), simpleQueryDTO.getValue());
        }else{
            retVal = QueryBuilders.matchQuery(simpleQueryDTO.getField(), simpleQueryDTO.getValue());
        }
        return retVal;
    }


    public static BoolQueryBuilder buildBooleanQuery(BooleanQueryDTO booleanQueryDTO) {
        processBooleanQuery(booleanQueryDTO);

        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        List<org.elasticsearch.index.query.QueryBuilder> queryBuilders = prepareQueryBuilders(booleanQueryDTO.getSimpleQueryDTOList());
        if(booleanQueryDTO.getOperation().equals("AND")){
            for(org.elasticsearch.index.query.QueryBuilder queryBuilder: queryBuilders){
                builder.must(queryBuilder);
            }
        }else{
            for(org.elasticsearch.index.query.QueryBuilder queryBuilder: queryBuilders){
                builder.should(queryBuilder);
            }
        }

        return builder;
    }

    public static List<org.elasticsearch.index.query.QueryBuilder> prepareQueryBuilders(List<SimpleQueryDTO> simpleQueryDTOList){
        List<org.elasticsearch.index.query.QueryBuilder> queryBuilders = new ArrayList<>();
        for(SimpleQueryDTO simpleQueryDTO: simpleQueryDTOList){
            queryBuilders.add(buildSimpleQuery(simpleQueryDTO, null));
        }
        return queryBuilders;
    }

    private static void processSimpleQuery(SimpleQueryDTO simpleQueryDTO){
        String errorMessage = "";
        if(simpleQueryDTO.getField() == null || simpleQueryDTO.getField().equals("")){
            errorMessage = "Field not specified";
        }

        if(simpleQueryDTO.getValue() == null){
            if(!errorMessage.equals("")) errorMessage += "\n";
            errorMessage += "Value not specified";
        }

        if(!errorMessage.equals("")){
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static void processBooleanQuery(BooleanQueryDTO booleanQueryDTO){
        String errorMessage = "";
        for(SimpleQueryDTO simpleQueryDTO: booleanQueryDTO.getSimpleQueryDTOList()){
            if(simpleQueryDTO.getField() == null || simpleQueryDTO.getField().equals("")){
                errorMessage = "Field not specified";
            }

            if(simpleQueryDTO.getValue() == null){
                if(!errorMessage.equals("")) errorMessage += "\n";
                errorMessage += "Value not specified";
            }

            if(!errorMessage.equals("")){
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }
}
