package com.transformer.es.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.elasticsearch.client.core.CountResponse;

/**
 * 运单跟踪搜索结果
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class CountResult<T> extends AbstractResult<T, CountResponse> {

}
