package com.upp.naucnacentrala.search;

import com.upp.naucnacentrala.client.GoogleClient;
import com.upp.naucnacentrala.dto.BooleanQueryDTO;
import com.upp.naucnacentrala.dto.SimpleQueryDTO;
import com.upp.naucnacentrala.model.Location;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import sun.java2d.pipe.SpanShapeRenderer;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    @Autowired
    private GoogleClient googleClient;

    public static  org.elasticsearch.index.query.QueryBuilder buildSimpleQuery(SimpleQueryDTO simpleQueryDTO, SearchType searchType){
        processSimpleQuery(simpleQueryDTO);
        org.elasticsearch.index.query.QueryBuilder retVal = null;
        if(simpleQueryDTO.getValue().startsWith("\"") && simpleQueryDTO.getValue().endsWith("\"")){
            System.out.println("FRAZA");
            retVal = QueryBuilders.matchPhraseQuery(simpleQueryDTO.getField(), simpleQueryDTO.getValue());
        }else{
            System.out.println("MATCH");
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

    public static BoolQueryBuilder buildGeoQuery(Location location){
        org.elasticsearch.index.query.QueryBuilder query = QueryBuilders.geoDistanceQuery("location").geoDistance(GeoDistance.ARC).point(location.getLatitude(), location.getLongitude()).distance("100km");
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.mustNot(query);
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
