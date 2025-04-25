package com.transformer.mq.consumer;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息通用监听器
 *
 * @author ouliyuan 2023/06/27
 */
@Slf4j
public abstract class MessageConsumer<T> {

    protected abstract T getMessage(String message);

    protected abstract boolean accept(T message);

}
