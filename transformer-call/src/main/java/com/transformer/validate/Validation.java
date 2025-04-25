package com.transformer.validate;

/**
 * @author caokai01
 * @date 2024/9/4
 */
public interface Validation {
    /**
     * 自定义校验（校验失败时，返回错误文案信息即可）
     */
    boolean validator();
}
