package com.transformer.context;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author only
 */
public class Environment implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static ConfigurableEnvironment envConfigure;
    private static final Map<String, String> ENV_MAP = new ConcurrentHashMap<>();

    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        setEnvironment(event.getEnvironment());
    }

    public void configChange() {
        ENV_MAP.clear(); // 清空缓存
    }

    public static String getProperty(String key) {
        return getOrLoadProperty(key, () -> getEnvironment().getProperty(key));
    }

    public static String getProperty(String key, String defaultValue) {
        return getOrLoadProperty(key, () -> getEnvironment().getProperty(key, defaultValue));
    }

    public static Integer getIntProperty(String key) {
        String value = getProperty(key);
        return value != null ? Integer.parseInt(value) : null;
    }

    public static Integer getIntProperty(String key, Integer defaultValue) {
        String value = getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public static boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return BooleanUtils.toBoolean(value);
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return BooleanUtils.toBooleanDefaultIfNull(BooleanUtils.toBooleanObject(value), defaultValue);
    }

    /**
     * 获取当前应用的 appId
     */
    public static String getApplicationId() {
        return com.zto.titans.common.env.EnvironmentManager.getAppName();
    }

    /**
     * 获取当前环境的注册中心地址
     */
    public static String getRegistryAddress() {
        return getProperty(com.zto.titans.common.env.EnvironmentManager.DUBBO_REGISTRY_ADDRESS);
    }

    /**
     * 判断当前环境是否为 dev 环境
     */
    public static boolean isDev() {
        return isEnv(Env.DEV);
    }

    /**
     * 判断当前环境是否为 fat 环境
     */
    public static boolean isFat() {
        return isEnv(Env.FAT);
    }

    /**
     * 判断当前环境是否为 pre 环境
     */
    public static boolean isPre() {
        return isEnv(Env.PRE);
    }

    /**
     * 判断当前环境是否为 pro 环境
     */
    public static boolean isProd() {
        return isEnv(Env.PRODUCT);
    }

    private static boolean isEnv(Env env) {
        String value = getOrLoadProperty(Env.ENV_NAME, Environment::getEnv);

        return StringUtils.containsIgnoreCase(value, env.getEnvName());
    }

    private static String getEnv() {
        String[] activeProfiles = getEnvironment().getActiveProfiles();
        if (ArrayUtils.isNotEmpty(activeProfiles)) {
            return activeProfiles[0];
        }
        return null;
    }

    private static String getOrLoadProperty(String key, Supplier<String> supplier) {
        // 优先从缓存中获取属性值
        String value = ENV_MAP.get(key);
        if (Objects.isNull(value)) {
            value = supplier.get();
            if (Objects.nonNull(value)) {
                ENV_MAP.put(key, value); // 将属性值放入缓存
            }
        }
        return value;
    }

    private static ConfigurableEnvironment getEnvironment() {
        return envConfigure;
    }

    private static void setEnvironment(ConfigurableEnvironment environment) {
        envConfigure = environment;
    }
}