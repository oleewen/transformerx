package com.transformer.es.agg;

import com.google.common.collect.Lists;
import com.transformer.helper.JsonHelper;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合：对原生ES聚合的封装、简化
 *
 * @author ouliyuan 2023/8/31
 */
@Data
public class Aggregation implements Serializable {
    /**
     * 聚合名
     */
    private String name;
    /**
     * 分桶
     */
    private List<Bucket> buckets;
    /**
     * 分桶合计数量
     */
    private long bucketCount;
    /**
     * 聚合统计值
     */
    private Long count;

    private String value;

    /**
     * 增加一个分桶
     *
     * @param key    分桶的key
     * @param value  分桶的统计值
     * @param parent 上一级分桶，无上一级时传入null
     * @return 增加的分桶
     */
    public Bucket addBucket(String key, Long value, Bucket parent) {
        if (this.buckets == null) {
            this.buckets = Lists.newArrayList();
        }

        Bucket bucket = new Bucket();
        bucket.setKey(key);
        bucket.setValue(value);
        bucket.setParent(parent);
        bucket.setName(name);

        this.buckets.add(bucket);

        this.bucketCount += value;

        return bucket;
    }

    /**
     * 根据聚合名查找聚合列表：会遍历子聚合
     *
     * @param name 聚合名
     * @return 聚合列表
     */
    public List<Aggregation> findAggregates(String name) {
        List<Aggregation> list = Lists.newArrayList();

        if (this.getName().equals(name)) {
            list.add(this);
        }
        List<Bucket> buckets = this.getBuckets();
        if (CollectionUtils.isNotEmpty(buckets)) {
            buckets.forEach(bucket -> {
                List<Aggregation> subAggregates = bucket.getSubAggregates();
                if (CollectionUtils.isNotEmpty(subAggregates)) {
                    for (Aggregation aggregate : subAggregates) {
                        List<Aggregation> subAggs = aggregate.findAggregates(name);
                        list.addAll(subAggs);
                    }
                }
            });
        }
        return list;
    }

    public long withoutBucketCount() {
        if (this.count == null) {
            return 0;
        }

        return this.count - this.bucketCount;
    }

    public String toString() {
        return JsonHelper.toJson(this);
    }
}
