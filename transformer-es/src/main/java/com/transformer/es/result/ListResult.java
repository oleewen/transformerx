package com.transformer.es.result;

import com.transformer.response.ListResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.Arrays;

public abstract class ListResult<T, R extends ListResponse<T>> extends SearchResult<R> {
    public R to() {
        R response = super.to();

        SearchHits hits = getResponse().getHits();
        if (hits != null) {
            // 设置total
            boolean hasHit = setTotalHits(response, hits);

            if (hasHit) {
                // 设置List
                setList(response, hits);
            }
        }

        return response;
    }

    protected boolean setTotalHits(R response, SearchHits hits) {
        TotalHits totalHits = hits.getTotalHits();
        if (totalHits != null) {
            long value = totalHits.value;
            response.setTotal(value);

            return value > 0;
        }
        return false;
    }

    protected void setList(R response, SearchHits hits) {
        SearchHit[] searchHits = hits.getHits();
        if (ArrayUtils.isNotEmpty(searchHits)) {
            Arrays.stream(searchHits).forEach(searchHit -> {
                T t = this.as(searchHit.getSourceAsString());
                response.addDTO(t);
            });
        }
    }

    protected abstract T as(String source);
}
