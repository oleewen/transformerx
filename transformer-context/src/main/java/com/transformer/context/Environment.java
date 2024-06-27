package com.transformer.context;

import com.transformer.exception.helper.ExceptionHelper;
import com.transformer.status.ResultCodeEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Properties;

/**
 * @author ouliyuan
 */
public class Environment implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    public static final String APP_ID = "app.id";
    public static final String REGISTRY_ADDRESS = "dubbo.registryAddress";
    private static ConfigurableEnvironment envConfigure;
    private static String appName;

    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        setEnvironment(event.getEnvironment());
    }

    public static String getEnvProperty(String key) {
        return getEnvironment().getProperty(key);
    }

    public static String getEnvProperty(String key, String defaultValue) {
        return getEnvironment().getProperty(key, defaultValue);
    }

    /**
     * 获取当前应用的appName
     */
    public static String getAppId() {
        if (appName == null) {
            String appId = System.getProperty(APP_ID);
            if (appId != null) {
                appName = appId;
            } else {
                Properties props = new Properties();

                try {
                    props.load(Environment.class.getResourceAsStream("/META-INF/app.properties"));
                } catch (Exception e) {
                    throw ExceptionHelper.createThrowableRuntimeException(ResultCodeEnum.INTERNAL_SERVER_ERROR, e);
                }

                appName = props.getProperty(APP_ID);
            }
        }

        return appName;
    }

    /**
     * 获取当前环境的注册中心地址
     */
    public static String getRegistryAddress() {
        return getEnvProperty(REGISTRY_ADDRESS);
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
     * 判断当前环境是否为 uat 环境
     */
    public static boolean isUat() {
        return isEnv(Env.PRE);
    }

    /**
     * 判断当前环境是否为 pro 环境
     */
    public static boolean isProd() {
        return isEnv(Env.PRODUCT);
    }

    private static boolean isEnv(Env env) {
        String[] activeProfiles = getEnvironment().getActiveProfiles();
        if (ArrayUtils.isEmpty(activeProfiles)) {
            return false;
        }
        String activeProfile = activeProfiles[0];
        return StringUtils.containsIgnoreCase(activeProfile, env.getEnvName());
    }

    private static ConfigurableEnvironment getEnvironment() {
        return envConfigure;
    }

    private static void setEnvironment(ConfigurableEnvironment environment) {
        envConfigure = environment;
    }
}