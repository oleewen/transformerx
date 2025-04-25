package com.transformer.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchResponse implements Serializable {
    /**
     * 请求id
     */
    private String requestId;
}
