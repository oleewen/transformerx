package com.transformer.es.helper;

import com.google.common.collect.Lists;
import com.transformer.es.agg.Aggregation;
import com.transformer.es.agg.Bucket;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.ParsedMultiBucketAggregation;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.ParsedValueCount;

import java.util.List;
import java.util.Objects;

/**
 * @author ouliyuan 2023/8/30
 */
public class AggregationHelper {
    public static final int BUCKET_SIZE = 65535;

    public static DateHistogramAggregationBuilder dateHistogram(String name) {
        return dateHistogram(name, DateHistogramInterval.DAY, "yyyy-MM-dd");
    }

    public static DateHistogramAggregationBuilder dateHistogram(String name, DateHistogramInterval interval, String format) {
        return AggregationBuilders.dateHistogram(name)
                .field(name)
                .calendarInterval(interval)
                .format(format);
    }

    public static FilterAggregationBuilder filter(String name, BoolQueryBuilder filter) {
        return AggregationBuilders.filter(name, filter);
    }

    public static TermsAggregationBuilder terms(String name) {
        return terms(name, BUCKET_SIZE);
    }

    public static TermsAggregationBuilder terms(String name, String field) {
        return AggregationBuilders.terms(name).field(field).size(BUCKET_SIZE);
    }

    public static TermsAggregationBuilder terms(String name, AggregationBuilder subAggregation) {
        return terms(name, BUCKET_SIZE, subAggregation);
    }

    public static TermsAggregationBuilder terms(String name, int size) {
        return AggregationBuilders.terms(name).field(name).size(size);
    }

    public static TermsAggregationBuilder terms(String name, int size, AggregationBuilder subAggregation) {
        TermsAggregationBuilder aggregation = AggregationBuilders.terms(name).field(name).size(size);
        aggregation.subAggregation(subAggregation);
        return aggregation;
    }

    public static List<Aggregation> findAggregate(List<Aggregation> aggregates, String aggName) {
        List<Aggregation> list = Lists.newArrayList();
        for (Aggregation aggregate : aggregates) {
            List<Aggregation> found = aggregate.findAggregates(aggName);
            list.addAll(found);
        }
        return list;
    }

    public static List<Aggregation> parseAggregations(Aggregations aggregations, Bucket parent) {
        List<Aggregation> list = Lists.newArrayList();
        for (org.elasticsearch.search.aggregations.Aggregation aggregation : aggregations) {
            Aggregation aggregate = new Aggregation();
            aggregate.setName(aggregation.getName());
            if (aggregation instanceof ParsedValueCount) {
                aggregate.setCount(((ParsedValueCount) aggregation).getValue());
                list.add(aggregate);
            } else if (aggregation instanceof ParsedSum) {
                aggregate.setValue(((ParsedSum) aggregation).getValueAsString());
                list.add(aggregate);
            } else if (aggregation instanceof ParsedFilter) {
                long docCount = ((ParsedFilter) aggregation).getDocCount();
                if (docCount > 0) {
                    List<Aggregation> subAggregates = parseAggregations(((ParsedFilter) aggregation).getAggregations(), parent);
                    if (CollectionUtils.isNotEmpty(subAggregates)) {
                        subAggregates.forEach(agg -> agg.setCount(docCount));
                        list.addAll(subAggregates);
                    } else {
                        aggregate.setCount(docCount);
                        list.add(aggregate);
                    }
                }
            } else {
                @SuppressWarnings("unchecked")
                final List<? extends MultiBucketsAggregation.Bucket> buckets = ((ParsedMultiBucketAggregation<ParsedDateHistogram.ParsedBucket>) aggregation).getBuckets();

                if(CollectionUtils.isNotEmpty(buckets)) {
                    for (MultiBucketsAggregation.Bucket bucket : buckets) {
                        Bucket addedBucket = aggregate.addBucket(bucket.getKeyAsString(), bucket.getDocCount(), parent);

                        if (Objects.nonNull(bucket.getAggregations())) {
                            List<Aggregation> subAggregates = parseAggregations(bucket.getAggregations(), addedBucket);
                            addedBucket.setSubAggregates(subAggregates);
                        }
                    }
                    list.add(aggregate);
                }
            }
        }
        return list;
    }
}
