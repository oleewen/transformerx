package com.transformer.es.result;

import com.transformer.es.agg.Aggregation;
import com.transformer.es.helper.AggregationHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * 运单跟踪搜索结果
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SearchResult<R extends com.transformer.response.SearchResponse> extends AbstractResult<R, SearchResponse> {
    public R to() {
        R response = newResponse();
        return to(response);
    }

    public R to(R response) {
        Assert.notNull(response, "response must not be null");

        response.setRequestId(getRequestId());

        List<Aggregation> aggregations = getAggregations();
        if (CollectionUtils.isNotEmpty(aggregations)) {
            setAggregations(response, aggregations);
        }

        return response;
    }

    protected abstract R newResponse();

    public List<Aggregation> getAggregations() {
        SearchResponse response = getResponse();
        // 解析聚合
        if (response != null && response.getAggregations() != null) {
            return AggregationHelper.parseAggregations(response.getAggregations(), null);
        }
        return Collections.emptyList();
    }

    protected void setAggregations(R response, List<Aggregation> aggregations) {
    }

}
