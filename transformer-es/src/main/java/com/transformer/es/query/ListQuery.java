package com.transformer.es.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * @author ouliyuan 2024/7/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class ListQuery extends SearchQuery {
    /**
     * offset
     */
    private String offset;

    @Override
    protected boolean isTrackTotalHits() {
        return true;
    }

    @Override
    protected Object[] getAfter() {
        if (StringUtils.isNotBlank(getOffset())) {
            return new String[]{getOffset()};
        }
        return super.getAfter();
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected SortBuilder getSort() {
        // 默认用billCode升序排序
        if (StringUtils.isNotBlank(getOffset())) {
            return new FieldSortBuilder("billCode").order(SortOrder.ASC);
        }
        return super.getSort();
    }
}
