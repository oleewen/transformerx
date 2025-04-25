package com.transformer.es.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ouliyuan 2024/7/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SearchQuery extends AbstractQuery {
    /**
     * 起始位置
     */
    private int from = 0;
    /**
     * 页码
     */
    private int size = 0;
}
