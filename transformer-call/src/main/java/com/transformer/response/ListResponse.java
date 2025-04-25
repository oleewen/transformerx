package com.transformer.response;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
public abstract class ListResponse<T> extends SearchResponse { //extends Response<R>{
    /**
     * 总量
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> list;

    public void addDTO(T t) {
        if (this.list == null) {
            this.list = Lists.newArrayList();
        }
        this.list.add(t);
    }
}
