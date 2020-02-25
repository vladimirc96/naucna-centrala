package com.upp.naucnacentrala.search;

import com.upp.naucnacentrala.client.GoogleClient;
import com.upp.naucnacentrala.dto.BooleanQueryDTO;
import com.upp.naucnacentrala.dto.SearchSciencePaperDTO;
import com.upp.naucnacentrala.dto.SimpleQueryDTO;
import com.upp.naucnacentrala.model.Location;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    @Autowired
    private GoogleClient googleClient;

    public static  SearchQuery buildSimpleQuery(SimpleQueryDTO simpleQueryDTO){
        processSimpleQuery(simpleQueryDTO);
        org.elasticsearch.index.query.QueryBuilder retVal = null;
        if(simpleQueryDTO.getValue().startsWith("\"") && simpleQueryDTO.getValue().endsWith("\"")){
            retVal = QueryBuilders.matchPhraseQuery(simpleQueryDTO.getField(), simpleQueryDTO.getValue());
        }else{
            retVal = QueryBuilders.matchQuery(simpleQueryDTO.getField(), simpleQueryDTO.getValue());
        }
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(retVal).withHighlightFields(new HighlightBuilder.Field(simpleQueryDTO.getField())).build();
        return searchQuery;
    }

    public static SearchQuery buildBooleanQuery(BooleanQueryDTO booleanQueryDTO) {
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

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(builder)
                .withHighlightFields(new HighlightBuilder.Field("magazineName"), new HighlightBuilder.Field("title"), new HighlightBuilder.Field("author")
                        ,new HighlightBuilder.Field("keyTerms"), new HighlightBuilder.Field("text"),
                        new HighlightBuilder.Field("scienceField")).build();

        return searchQuery;
    }

    public static BoolQueryBuilder buildGeoQuery(Location location){
        org.elasticsearch.index.query.QueryBuilder query = QueryBuilders.geoDistanceQuery("location").geoDistance(GeoDistance.ARC).point(location.getLatitude(),
                location.getLongitude()).distance("100km");
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.mustNot(query);
        return builder;
    }

    public static SearchQuery buildMoreLikeThisQuery(String parsedText){
        MoreLikeThisQueryBuilder query = QueryBuilders.moreLikeThisQuery(new String[] { "text"}, new String[] { parsedText }, null)
                .minTermFreq(1).maxQueryTerms(12);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
        return searchQuery;
    }

    public static org.elasticsearch.index.query.QueryBuilder buildGetReviewersQuery(List<SearchSciencePaperDTO> sciencePaperDTOList){
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        for(SearchSciencePaperDTO sciencePaperDTO: sciencePaperDTOList){
            builder.should(QueryBuilders.matchQuery("sciencePapers.title", sciencePaperDTO.getTitle()));
        }
        org.elasticsearch.index.query.QueryBuilder queryBuilder = QueryBuilders.nestedQuery("sciencePapers", builder, ScoreMode.Avg);
        return queryBuilder;
    }

    public static List<org.elasticsearch.index.query.QueryBuilder> prepareQueryBuilders(List<SimpleQueryDTO> simpleQueryDTOList){
        List<org.elasticsearch.index.query.QueryBuilder> queryBuilders = new ArrayList<>();
        for(SimpleQueryDTO simpleQueryDTO: simpleQueryDTOList){
            queryBuilders.add(build(simpleQueryDTO));
        }
        return queryBuilders;
    }

    public static org.elasticsearch.index.query.QueryBuilder build(SimpleQueryDTO simpleQueryDTO){
        processSimpleQuery(simpleQueryDTO);
        org.elasticsearch.index.query.QueryBuilder retVal = null;
        if(simpleQueryDTO.getValue().startsWith("\"") && simpleQueryDTO.getValue().endsWith("\"")){
            retVal = QueryBuilders.matchPhraseQuery(simpleQueryDTO.getField(), simpleQueryDTO.getValue());
        }else{
            retVal = QueryBuilders.matchQuery(simpleQueryDTO.getField(), simpleQueryDTO.getValue());
        }
        return retVal;
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
