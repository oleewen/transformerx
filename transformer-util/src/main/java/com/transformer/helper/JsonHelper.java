package com.transformer.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.transformer.exception.helper.ExceptionHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Json工具类，提供常用的json序列化和反序列化方法
 * <p>
 * 公共类库，修改或增加能力，请联系author
 * </p>
 *
 * @author ouliyuan 2023/06/30
 * @see ObjectMapper
 */
@Slf4j
public final class JsonHelper {
    private ObjectMapper instance;

    private JsonHelper() {
    }

    private static final class InstanceHolder {
        // Singleton instance: 所有属性（无论属性值是否为空）
        private static final ObjectMapper INSTANCE_OF_SNAKE_WITH_EMPTY;
        private static final ObjectMapper INSTANCE_OF_CAMEL_WITH_EMPTY;
        // Singleton instance: 非空属性
        private static final ObjectMapper INSTANCE_OF_CAMEL_WITHOUT_EMPTY;

        private static final String STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss";
        private static final String DATE_PATTERN = "yyyy-MM-dd";
        private static final String TIME_PATTERN = "HH:mm:ss";

        static {
            // 初始化JavaTimeModule
            JavaTimeModule javaTimeModule = new JavaTimeModule();

            // 处理LocalDateTime
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_PATTERN);
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

            // 处理LocalDate
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

            // 处理LocalTime
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
            javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

            INSTANCE_OF_CAMEL_WITH_EMPTY = new ObjectMapper()
                    // 包含对象的所有属性字段
                    .setSerializationInclusion(JsonInclude.Include.ALWAYS)
                    // 忽略空bean转json的错误
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    // 忽略未知属性字段
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    // 取消默认转换为timestamps的形式
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    // 所有的日期格式统一格式化为以下样式 yyyy-MM-dd HH:mm:ss
                    .setDateFormat(new SimpleDateFormat(STANDARD_PATTERN))
                    .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                    // 注册时间模块, 支持jsr310, 即新的时间类(java.time包下的时间类)
                    .registerModule(javaTimeModule);

            INSTANCE_OF_SNAKE_WITH_EMPTY = new ObjectMapper()
                    // 包含对象的所有属性字段
                    .setSerializationInclusion(JsonInclude.Include.ALWAYS)
                    // 忽略空bean转json的错误
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    // 忽略未知属性字段
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    // 取消默认转换为timestamps的形式
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    // 所有的日期格式统一格式化为以下样式 yyyy-MM-dd HH:mm:ss
                    .setDateFormat(new SimpleDateFormat(STANDARD_PATTERN))
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    // 注册时间模块, 支持jsr310, 即新的时间类(java.time包下的时间类)
                    .registerModule(javaTimeModule);

            INSTANCE_OF_CAMEL_WITHOUT_EMPTY = new ObjectMapper()
                    // 包含对象的非空属性字段
                    .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                    // 忽略空bean转json的错误
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    // 忽略未知属性字段
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    // 取消默认转换为timestamps的形式
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                    // 所有的日期格式统一格式化为以下样式 yyyy-MM-dd HH:mm:ss
                    .setDateFormat(new SimpleDateFormat(STANDARD_PATTERN))
                    // 注册时间模块, 支持jsr310, 即新的时间类(java.time包下的时间类)
                    .registerModule(javaTimeModule);
        }

    }

    public static JsonHelper getSnakeInstance() {
        return JsonHelper.create(InstanceHolder.INSTANCE_OF_SNAKE_WITH_EMPTY);
    }

    public static JsonHelper getCamelInstance() {
        return JsonHelper.create(InstanceHolder.INSTANCE_OF_CAMEL_WITH_EMPTY);
    }

    public static JsonHelper getNonEmptyInstance() {
        return JsonHelper.create(InstanceHolder.INSTANCE_OF_CAMEL_WITHOUT_EMPTY);
    }

    private static JsonHelper create(ObjectMapper objectMapper) {
        JsonHelper jsonHelper = new JsonHelper();
        jsonHelper.instance = objectMapper;
        return jsonHelper;
    }

    public static <T> String toJson(T value) {
        return JsonHelper.getSnakeInstance().json(value);
    }

    public static <T> String toPrettyJson(T value) {
        return JsonHelper.getSnakeInstance().jsonPretty(value);
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        return JsonHelper.getSnakeInstance().parse(json, clazz);
    }

    public static <T> T parseObject(String json, TypeReference<T> type) {
        return JsonHelper.getSnakeInstance().parse(json, type);
    }

    public static <T> T parseObject(InputStream src, Class<T> clazz) {
        return JsonHelper.getSnakeInstance().parse(src, clazz);
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        return JsonHelper.getSnakeInstance().parseAsList(json, clazz);
    }

    public static <T> Set<T> parseSet(String json, Class<T> clazz) {
        return JsonHelper.getSnakeInstance().parseAsSet(json, clazz);
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return JsonHelper.getSnakeInstance().parseAsMap(json, keyClass, valueClass);
    }

    public static Map<String, Object> parseMap(String json) {
        return JsonHelper.getSnakeInstance().parseAsMap(json);
    }

    public <T> String json(T value) {
        try {
            return instance.writeValueAsString(value);
        } catch (Exception e) {
            throw ExceptionHelper.createThrowableException(e);
        }
    }

    public <T> String jsonPretty(T value) {
        try {
            return instance.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            throw ExceptionHelper.createThrowableException(e);
        }
    }

    public <T> T parse(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || Objects.isNull(clazz)) {
            return null;
        }

        try {
            return instance.readValue(json, clazz);
        } catch (Exception e) {
            log.error("parse " + json + " to " + clazz + " class exception", e);
            return null;
        }
    }

    public <T> T parse(String json, TypeReference<T> type) {
        if (StringUtils.isBlank(json) || Objects.isNull(type)) {
            return null;
        }

        try {
            return instance.readValue(json, type);
        } catch (Exception e) {
            log.error("parse " + json + " to " + type + " type exception", e);
            return null;
        }
    }

    public <T> T parse(InputStream src, Class<T> clazz) {
        if (Objects.isNull(src) || Objects.isNull(clazz)) {
            return null;
        }

        try {
            return instance.readValue(src, clazz);
        } catch (Exception e) {
            log.error("parse stream to " + clazz + " exception", e);
            return null;
        }
    }

    public <T> List<T> parseAsList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || Objects.isNull(clazz)) {
            return Collections.emptyList();
        }

        JavaType javaType = instance.getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            return instance.readValue(json, javaType);
        } catch (Exception e) {
            log.error("parseList:exception", e);
            return Collections.emptyList();
        }
    }

    public <T> Set<T> parseAsSet(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || Objects.isNull(clazz)) {
            return Collections.emptySet();
        }

        JavaType javaType = instance.getTypeFactory().constructCollectionType(Set.class, clazz);
        try {
            return instance.readValue(json, javaType);
        } catch (Exception e) {
            log.error("parseSet:exception", e);
            return Collections.emptySet();
        }
    }

    public <K, V> Map<K, V> parseAsMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isBlank(json) || Objects.isNull(keyClass) || Objects.isNull(valueClass)) {
            return Collections.emptyMap();
        }

        JavaType javaType = instance.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        try {
            return instance.readValue(json, javaType);
        } catch (Exception e) {
            log.error("parseMap:exception", e);
            return Collections.emptyMap();
        }
    }

    public Map<String, Object> parseAsMap(String json) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyMap();
        }

        MapLikeType mapLikeType = instance.getTypeFactory().constructMapLikeType(Map.class, String.class, Object.class);
        try {
            return instance.readValue(json, mapLikeType);
        } catch (Exception e) {
            log.error("parseMap:exception", e);
            return Collections.emptyMap();
        }
    }
}
