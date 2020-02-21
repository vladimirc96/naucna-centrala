package com.upp.naucnacentrala.search;

import com.upp.naucnacentrala.dto.SimpleQueryDTO;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;

public class QueryBuilder {

    public static  org.elasticsearch.index.query.QueryBuilder buildSimpleQuery(SimpleQueryDTO simpleQueryDTO, SearchType searchType){
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
        org.elasticsearch.index.query.QueryBuilder retVal = null;
        retVal = QueryBuilders.matchPhraseQuery(simpleQueryDTO.getField(), simpleQueryDTO.getValue());
        return retVal;
    }




}
