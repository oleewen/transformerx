package com.transformer.mq.consumer;

import com.zto.consumer.MsgConsumedStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import com.transformer.status.Status;
import com.transformer.status.StatusCode;

/**
 * RocketMQ消息消费者
 *
 * @author ouliyuan 2023/7/18
 * eg
 * public class XXXXMessageConsumer extends RocketMQMessageConsumer<XXXXMessage> {
 * private static final String TRANSFER_OPERATE_SCAN_CONSUMER = "transporting_zto_operate_bag_consumer";
 * @Resource private BagOperationApplicationService bagOperationApplicationService;
 * @ZMSListener(consumerGroup = "XXXX_CONSUMER_GROUP", consumeThreadMin = "5", consumeThreadMax = "10")
 * public ZMSResult listener(
 * @ZMSListenerParameter(name = MQMsgEnum.CONSUMER_GROUP) String group,
 * @ZMSListenerParameter(name = MQMsgEnum.QUEUE_OFFSET) String offset,
 * @ZMSListenerParameter(name = MQMsgEnum.QUEUE_ID) String queueId,
 * @ZMSListenerParameter(name = MQMsgEnum.MSG_ID) String msgId,
 * @ZMSListenerParameter(name = MQMsgEnum.BODY) String body,
 * @ZMSListenerParameter(name = MQMsgEnum.TAG) String tag,
 * @ZMSListenerParameter(name = MQMsgEnum.RECONSUME_TIMES) String retryTimes){
 * <p>
 * MsgConsumedStatus status = consumer(group, queueId, offset, msgId, body, tag, retryTimes);
 * return ZMSResult.status(status);
 * }
 * @Override protected XXXXXMessage getMessage(String message) {
 * return JSON.parseObject(message, XXXXXMessage.class);
 * }
 * @Override protected boolean accept(BagCenterMessage message) {
 * ....applicationService do process
 * }
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
    public MsgConsumedStatus consumer(String group, String queueId, String offset, String msgId, String body, String tag, String retryTimes) {
        MsgConsumedStatus status;
        try {
            long start = System.currentTimeMillis();
            // 处理消息
            boolean success = handler(body, tag);

            // 记录处理日志
            logger(group, queueId, offset, msgId, body, tag, retryTimes, success, start);

            status = success ? MsgConsumedStatus.SUCCEED : MsgConsumedStatus.RETRY;
        } catch (IllegalArgumentException e) {
            // 记录异常日志
            error(group, queueId, offset, msgId, body, tag, retryTimes, "illegal argument", e);

            status = MsgConsumedStatus.SUCCEED;
        } catch (Exception e) {
            // 记录异常日志
            error(group, queueId, offset, msgId, body, tag, retryTimes, "exception", e);

            status = MsgConsumedStatus.RETRY;
        }

        // 不成功时，尝试再次投递
        if (status != MsgConsumedStatus.SUCCEED) {
            return retry(retryTimes);
        }

        // 成功消费返回成功
        return status;
    }

    protected boolean handler(String body, String tag) {
        // 消息体转消息对象
        T message;
        try {
            message = getMessage(body);
        } catch (Exception e) {
            throw new IllegalArgumentException("message parse to object exception", e);
        }

        // 判断消息是否应该被接受
        if (shouldAccept(message, tag)) {
            return accept(message);
        }

        log.warn("message should not be accepted:{}", body);
        return false;
    }

    /**
     * 判断消息是否应该被接受
     *
     * @param message 消息对象
     * @param tag 消息标签
     * @return true: 应该接受; false: 不应该接受
     */
    protected boolean shouldAccept(T message, String tag) {
        Status status = validate(message, tag);
        if (status.isSuccess()) {
            return true;
        }

        log.warn("validate message failed:{}, message:{}", status.getMessage(), message);
        return false;
    }

    /**
     * 验证消息
     *
     * @param message 消息对象
     * @param tag 消息标签
     * @return 验证结果
     */
    protected Status validate(T message, String tag) {
        if (Objects.isNull(message)) {
            return StatusCode.BAD_REQUEST.with("message is null");
        }

        if (StringUtils.isNotBlank(tag)) {
            boolean success = filterTag(message, tag);

            if (!success) {
                return StatusCode.NOT_ACCEPTABLE.with("tag is not accepted");
            }
        }

        return StatusCode.OK;
    }

    protected boolean filterTag(T message, String tag) {
        return true;
    }

    private void error(String group, String queueId, String offset, String msgId, String body, String tag, String retryTimes, String status, Exception e) {
        log.error("consume " + status + ", group:{}, queueId:{}, offset:{}, msgId:{}, tag:{}, body:{}, retryTimes:{}", group, queueId, offset, msgId, tag, body, retryTimes, e);
    }

    private void logger(String group, String queueId, String offset, String msgId, String body, String tag, String retryTimes, boolean success, long startTime) {
        log.warn("consume {}, cost:{}ms, group:{}, queueId:{}, offset:{}, msgId:{}, tag:{}, body:{}, retryTimes:{}", success ? "success" : "failure", System.currentTimeMillis() - startTime, group, queueId, offset, msgId, tag, body, retryTimes);
    }

    protected MsgConsumedStatus retry(String retryTimes) {
        // 需要重试时，根据投递消费次数递增再次投递时间
        if (StringUtils.isNotBlank(retryTimes)) {
            int times = Integer.parseInt(retryTimes);
            for (MsgConsumedStatus each : MsgConsumedStatus.values()) {
                if (each.getLevel() == times) {
                    return each;
                }
            }
            return MsgConsumedStatus.SUCCEED;
        } else {
            return MsgConsumedStatus.RETRY_1S;
        }
    }
}
