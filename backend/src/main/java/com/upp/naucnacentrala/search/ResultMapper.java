package com.upp.naucnacentrala.search;

import com.upp.naucnacentrala.model.SciencePaperES;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultMapper implements SearchResultMapper {

    private HighlightBuilder.Field[] fields;
    private Map<String, String> map = new HashMap<String, String>(){{
        put("magazineName", "Časopis");
        put("title", "Naslov rada");
        put("keyTerms", "Ključni pojam");
        put("text", "Sadržaj PDF-a");
        put("scienceField", "Naučna oblast");
        put("author", "Ime i prezime autora");
    }};


    public ResultMapper() {
    }

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
        List<SciencePaperES> chunk = new ArrayList<SciencePaperES>();
        for (SearchHit searchHit : searchResponse.getHits()) {
            if (searchResponse.getHits().getHits().length <= 0) {
                return null;
            }
            Map<String, Object> source = searchHit.getSourceAsMap();
            SciencePaperES sciencePaperES = new SciencePaperES();
            sciencePaperES.setId((String) source.get("id"));
            sciencePaperES.setDoi((String) source.get("doi"));
            sciencePaperES.setTitle((String) source.get("title"));
            sciencePaperES.setMagazineName((String) source.get("magazineName"));
            sciencePaperES.setKeyTerms((String) source.get("keyTerms"));
            sciencePaperES.setPaperAbstract((String) source.get("paperAbstract"));
            sciencePaperES.setScienceField((String) source.get("scienceField"));
            sciencePaperES.setText((String) source.get("text"));
            sciencePaperES.setFilePath((String) source.get("filePath"));
            sciencePaperES.setAuthor((String) source.get("author"));

            String highlights = "";
            for(HighlightBuilder.Field field : this.fields){
                if(searchHit.getHighlightFields().get(field.name()) == null){
                    continue;
                }
                highlights += this.map.get(field.name()) + ": " + searchHit.getHighlightFields().get(field.name()).fragments()[0].toString() +  ". ";
            }
            sciencePaperES.setHighlight(highlights);
            chunk.add(sciencePaperES);
        }
        if (chunk.size() > 0) {
            return new AggregatedPageImpl(chunk);
        }
        return null;
    }


    @Override
    public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
        return null;
    }

    public HighlightBuilder.Field[] getFields() {
        return fields;
    }

    public void setFields(HighlightBuilder.Field[] fields) {
        this.fields = fields;
    }
}
