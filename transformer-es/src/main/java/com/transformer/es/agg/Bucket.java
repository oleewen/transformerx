package com.transformer.es.agg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transformer.helper.JsonHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合分桶
 *
 * @author ouliyuan 2023/8/31
 */
@Data
public class Bucket implements Serializable {
    /**
     * 聚合名
     */
    private String name;
    /**
     * 分桶key
     */
    private String key;
    /**
     * 统计值
     */
    private Long value;
    /**
     * 父分桶
     */
    @JsonIgnore
    private Bucket parent;
    /**
     * 子聚合列表
     */
    private List<Aggregation> subAggregates;

    public String toString() {
        return JsonHelper.getNonEmptyInstance().json(this);
    }

}
