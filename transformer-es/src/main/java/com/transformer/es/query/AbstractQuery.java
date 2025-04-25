package com.transformer.es.query;

import com.transformer.es.index.Indexes;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractQuery implements Query<SearchSourceBuilder, Indexes> {
    @Override
    public SearchSourceBuilder getBuilder() {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        searchBuilder.trackTotalHits(isTrackTotalHits());
        searchBuilder.trackScores(isTrackScores());

        searchBuilder.size(getSize());
        if (ArrayUtils.isNotEmpty(getAfter())) {
            searchBuilder.searchAfter(getAfter());
        } else if (getFrom() > 0) {
            searchBuilder.from(getFrom());
        }

        if (Objects.nonNull(getSort())) {
            searchBuilder.sort(getSort());
        }

        String[] includes = includeFields();
        String[] excludes = excludeFields();
        if (Objects.nonNull(includes) || Objects.nonNull(excludes)) {
            searchBuilder.fetchSource(includes, excludes);
        }

        BoolQueryBuilder query = buildQuery();
        if (query != null) {
            searchBuilder.query(query);
        }

        List<AggregationBuilder> aggregations = buildAggregations();
        if (CollectionUtils.isNotEmpty(aggregations)) {
            aggregations.forEach(searchBuilder::aggregation);
        }

        return searchBuilder;
    }

    protected boolean isTrackTotalHits() {
        return false;
    }

    protected boolean isTrackScores() {
        return false;
    }

    public int getFrom() {
        return 0;
    }

    public int getSize() {
        return 0;
    }

    protected Object[] getAfter() {
        return null;
    }

    @SuppressWarnings("rawtypes")
    protected SortBuilder getSort() {
        return null;
    }

    protected String[] includeFields() {
        return null;
    }

    protected String[] excludeFields() {
        return null;
    }

    protected abstract BoolQueryBuilder buildQuery();

    protected List<AggregationBuilder> buildAggregations() {
        return Collections.emptyList();
    }

}
