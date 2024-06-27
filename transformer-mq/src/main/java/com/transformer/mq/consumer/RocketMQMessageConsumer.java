package com.transformer.mq.consumer;

import com.transformer.mq.message.MessageConsumedStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * RocketMQ消息消费者
 *
 * @author ouliyuan 2023/7/18
 */
@Slf4j
public abstract class RocketMQMessageConsumer<T> extends MessageConsumer<T> {

    /**
     * 消费MQ消息
     *
     * @param group
     * @param queueId
     * @param offset
     * @param msgId      消息id
     * @param body       消息体
     * @param tag        消息tag
     * @param retryTimes 重试次数
     * @return 是否成功
     */
    public MessageConsumedStatus consumer(String group, String queueId, String offset, String msgId, String body, String tag, String retryTimes) {
        MessageConsumedStatus status;
        try {
            long start = System.currentTimeMillis();
            // 处理消息
            boolean success = handler(body);

            // 记录处理日志
            logger(group, queueId, offset, msgId, body, tag, retryTimes, success, start);

            status = success ? MessageConsumedStatus.SUCCEED : MessageConsumedStatus.RETRY;
        } catch (IllegalArgumentException e) {
            // 记录异常日志
            error(group, queueId, offset, msgId, body, tag, retryTimes, "illegal argument", e);

            status = MessageConsumedStatus.SUCCEED;
        } catch (Exception e) {
            // 记录异常日志
            error(group, queueId, offset, msgId, body, tag, retryTimes, "exception", e);

            status = MessageConsumedStatus.RETRY;
        }

        // 不成功时，尝试再次投递
        if (status != MessageConsumedStatus.SUCCEED) {
            return retry(retryTimes);
        }

        // 成功消费返回成功
        return status;
    }

    private void error(String group, String queueId, String offset, String msgId, String body, String tag, String retryTimes, String status, Exception e) {
        log.error("consume " + status + ", group:{}, queueId:{}, offset:{}, msgId:{}, tag:{}, body:{}, retryTimes:{}", group, queueId, offset, msgId, tag, body, retryTimes, e);
    }

    private void logger(String group, String queueId, String offset, String msgId, String body, String tag, String retryTimes, boolean success, long startTime) {
        log.warn("consume {}, cost:{}ms, group:{}, queueId:{}, offset:{}, msgId:{}, tag:{}, body:{}, retryTimes:{}", success ? "success" : "failure", System.currentTimeMillis() - startTime, group, queueId, offset, msgId, tag, body, retryTimes);
    }

    protected MessageConsumedStatus retry(String retryTimes) {
        // 需要重试时，根据投递消费次数递增再次投递时间
        if (StringUtils.isNotBlank(retryTimes)) {
            int times = Integer.parseInt(retryTimes);
            for (MessageConsumedStatus each : MessageConsumedStatus.values()) {
                if (each.getLevel() == times) {
                    return each;
                }
            }
            return MessageConsumedStatus.SUCCEED;
        } else {
            return MessageConsumedStatus.RETRY_1S;
        }
    }
}
