package com.transformer.mq.message;

/**
 * @author ouliyuan 2024/6/27
 */
public enum MessageConsumedStatus {

    /**
     * 消息消费成功返回状态
     */
    SUCCEED(-1),

    /**
     * 消息消费失败后，下一次重新消费的时间间隔状态
     * 其中，RETRY(0) 由 broker 控制重试间隔
     * 其余由客户端控制重新消费间隔，比如：
     * RETRY_10S(3)，则该消息 10S 后将再次重新消费
     *
     * 请注意：
     * Kafka 不支持消息重消费，因此，如果是 Kafka，无论返回哪个时间间隔，消息都不会重新消费，
     * 如果你的 Kafka 消费客户端消费失败后，返回 RETRY_XXS，将会造成该条消息丢失。
     */
    RETRY(0),
    RETRY_1S(1),
    RETRY_5S(2),
    RETRY_10S(3),
    RETRY_30S(4),
    RETRY_1M(5),
    RETRY_2M(6),
    RETRY_3M(7),
    RETRY_4M(8),
    RETRY_5M(9),
    RETRY_6M(10),
    RETRY_7M(11),
    RETRY_8M(12),
    RETRY_9M(13),
    RETRY_10M(14),
    RETRY_20M(15),
    RETRY_30M(16),
    RETRY_1H(17),
    RETRY_2H(18);

    int level;

    MessageConsumedStatus(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
