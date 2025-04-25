package com.transformer.helper;

import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.annotation.PreDestroy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * 日期工具类
 * <p>
 * 公共类库，修改或增加能力，请联系author
 * </p>
 *
 * @author ouliyuan 2023-06-26
 */
public abstract class DateHelper extends org.apache.commons.lang3.time.DateUtils {
    private static final String MONTH_FORMAT_PATTERN = "yyyy-MM";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private static final String MINUTE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    private static final String TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String SIMPLE_DATE_MINUTE_PATTERN = "yyyyMMddHHmm";
    private static final String TIME_MINUTE_FORMAT_PATTERN = "HH:mm";
    private static final String TIME_SECOND_FORMAT_PATTERN = "HH:mm:ss";

    private static final ThreadLocal<SimpleDateFormat> MONTH_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(MONTH_FORMAT_PATTERN));
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE_FORMAT_PATTERN));
    private static final ThreadLocal<SimpleDateFormat> MINUTE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(MINUTE_FORMAT_PATTERN));
    private static final ThreadLocal<SimpleDateFormat> TIME_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(TIME_FORMAT_PATTERN));
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_TIME_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(SIMPLE_DATE_MINUTE_PATTERN));
    private static final ThreadLocal<SimpleDateFormat> TIME_MINUTE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(TIME_MINUTE_FORMAT_PATTERN));
    private static final ThreadLocal<SimpleDateFormat> TIME_SECOND_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(TIME_SECOND_FORMAT_PATTERN));

    private static final String NULL_VALUE = "1900-1-1 00:00:00";

    private DateHelper(){}

    /**
     * 取指定日期的年
     *
     * @param date 日期
     * @return 日期对应的年
     */
    public static int getYear(Date date) {
        return get(date, Calendar.YEAR);
    }

    /**
     * 取指定日期的月
     *
     * @param date 日期
     * @return 日期对应的月
     */
    public static int getMonth(Date date) {
        return get(date, Calendar.MONTH);
    }

    /**
     * 取指定日期的天
     *
     * @param date 日期
     * @return 日期对应的天
     */
    public static int getDay(Date date) {
        return get(date, Calendar.DAY_OF_MONTH);
    }

    /**
     * 取指定日期是星期几
     * @param date 日期
     * @return 星期几，1代表星期日，7代表星期六
     */
    public static int getWeekDay(Date date) {
        return get(date, Calendar.DAY_OF_WEEK);
    }

    private static int get(Date date, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(field);
    }

    /**
     * 获取某年第一天开始时间
     *
     * @param year 年份
     * @return 某年第一天开始时间
     */
    public static Date getYearStartTime(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取某年最后一天最后时间
     *
     * @param year 年份
     * @return 某年最后一天最后时间，精确到毫秒
     */
    public static Date getYearEndTime(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.MILLISECOND, -1);

        return calendar.getTime();
    }

    /**
     * 获取指定时间当月第一天
     *
     * @param date 指定时间，传入空则取系统当前时间
     * @return 指定时间当月第一天日期
     */
    public static Date getMonthStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            date = new Date();
        }
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定时间当月最后一天
     *
     * @param date 指定时间，传入空则取系统当前时间
     * @return 指定时间当月最后一天最后时间，精确到毫秒
     */
    public static Date getMonthEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            date = new Date();
        }
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取某天开始时间
     *
     * @param date 某天，传入空则取系统当前时间
     * @return 开始时间
     */
    public static Date getDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            date = new Date();
        }
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某天结束时间
     *
     * @param date 某天，传入空则取系统当前时间
     * @return 结束时间
     */
    public static Date getDayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            date = new Date();
        }
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 设置日期
     *
     * @param date  日期
     * @param year  年份
     * @param month 月份
     * @param day   月天
     * @return 日期
     */
    public static Date setDate(Date date, int year, int month, int day) {
        Date d = setYears(date, year);
        d = setMonths(d, month);
        d = setDays(d, day);
        return d;
    }

    /**
     * 判断是否是1900年1月1日0点0分0秒
     *
     * @param date 日期
     * @return 是否为1900年1月1日0点0分0秒
     */
    public static boolean is1900(Date date) {
        if (date == null) {
            return true;
        }
        Date nullValue = parseTime(NULL_VALUE);
        return date.getTime() == nullValue.getTime();
    }

    /**
     * 转yyyy-MM-dd格式字符串为日期
     *
     * @param date 日期字符串
     * @return 日期
     */
    public static Date parseDate(String date) {
        return parse(date, DATE_FORMAT_PATTERN);
    }

    /**
     * 转yyyy-MM-dd HH:mm:ss格式字符串为日期
     * @param date 日期字符串
     * @return 日期
     */
    public static Date parseTime(String date) {
        return parse(date, TIME_FORMAT_PATTERN);
    }

    /**
     * 转yyyy-MM-dd HH:mm格式字符串为日期
     *
     * @param date 日期字符串
     * @return 日期
     */
    public static Date parseTimeMinute(String date) {
        return parse(date, MINUTE_FORMAT_PATTERN);
    }

    /**
     * 按照日期格式，将字符串解析为日期对象
     *
     * @param date    一个按pattern格式排列的日期的字符串描述
     * @param pattern 输入字符串的格式
     * @return Date 对象
     * @throws ParseException
     * @see SimpleDateFormat
     */
    private static Date parse(String date, String pattern) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(date, pattern);
        } catch (ParseException pe) {
            throw new IllegalArgumentException(pe);
        }
    }

    /**
     * 将LocalDate转换为Date
     *
     * @param localDate LocalDate类型到时间
     * @return Date时间
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将LocalTimeDate转换为Date
     *
     * @param localDateTime LocalDateTime类型时间
     * @return Date时间
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将Date转换为LocalDate
     *
     * @param date Date类型到时间
     * @return LocalDate时间
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) return null;
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 将Date转换为LocalTimeDate
     *
     * @param date Date类型时间
     * @return LocalTimeDate时间
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将日期格式化为时间yyyy-MM-dd格式
     *
     * @param date 日期
     * @return yyyy-MM-dd格式的日期字符串
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }

        return DATE_FORMAT.get().format(date);
    }

    /**
     * 将日期格式化为时间yyyy-MM格式
     *
     * @param date 日期
     * @return yyyy-MM格式的日期字符串
     */
    public static String formatMonth(Date date) {
        if (date == null) {
            return "";
        }

        return MONTH_FORMAT.get().format(date);
    }

    /**
     * 将日期格式化为时间yyyy-MM-dd HH:mm:ss格式
     *
     * @param date 日期
     * @return yyyy-MM-dd HH:mm:ss格式的日期字符串
     */
    public static String formatTime(Date date) {
        if (date == null) {
            return "";
        }

        return TIME_FORMAT.get().format(date);
    }

    /**
     * 将日期格式化为时间yyyyMMddHHmm格式
     *
     * @param date 日期
     * @return yyyyMMddHHmm格式的日期字符串
     */
    public static String formatSimpleTime(Date date) {
        if (date == null) {
            return "";
        }
        return SIMPLE_TIME_FORMAT.get().format(date);
    }

    /**
     * 将日期格式化为时间yyyy-MM-dd HH:mm格式
     *
     * @param str 日期字符串
     * @return 日期
     */
    public static Date parseSimpleTime(String str) {
        if (str == null) {
            return null;
        }
        try {
            return SIMPLE_TIME_FORMAT.get().parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将日期格式化为时间yyyy-MM-dd HH:mm格式
     *
     * @param date 日期
     * @return yyyy-MM-dd HH:mm
     */
    public static String formatMinute(Date date) {
        if (date == null) {
            return "";
        }

        return MINUTE_FORMAT.get().format(date);
    }

    /**
     * 将日期格式化为时间HH:mm:ss格式
     *
     * @param date 日期
     * @return HH:mm:ss
     */
    public static String formatTimeSecond(Date date) {
        if (date == null) {
            return "";
        }

        return TIME_SECOND_FORMAT.get().format(date);
    }

    /**
     * 将日期格式化为时间HH:mm格式
     *
     * @param date 日期
     * @return HH:mm
     */
    public static String formatTimeMinute(Date date) {
        if (date == null) {
            return "";
        }

        return TIME_MINUTE_FORMAT.get().format(date);
    }

    /**
     * 将本年的第一天格式化为yyyy-MM-dd格式
     *
     * @param year 某某年
     * @return 这年第一天的yyyy-MM-dd格式日期字符串
     */
    public static String formatYearFirstDay(int year) {
        return formatDate(getYearStartTime(year));
    }

    /**
     * 本年的最后一天格式化为yyyy-MM-dd格式
     *
     * @param year 某某年
     * @return 这年最后一天的yyyy-MM-dd格式日期字符串
     */
    public static String formatYearLastDay(int year) {
        return formatDate(getYearEndTime(year));
    }

    /**
     * 计算两个日期之间相差的年数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 相差年数
     */
    public static int years(Date start, Date end) {
        return period(start, end, "y");
    }

    /**
     * 计算两个日期之间相差的月数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 相差月数
     */
    public static int months(Date start, Date end) {
        return period(start, end, "M");
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 相差天数
     */
    public static int days(Date start, Date end) {
        return period(start, end, "d");
    }

    /**
     * 计算两个日期之间相差的小时数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 相差小时数
     */
    public static int hours(Date start, Date end) {
        return period(start, end, "H");
    }

    /**
     * 计算时间分钟差
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static int minutes(Date start, Date end) {
        return period(start, end, "m");
    }

    /**
     * 计算时间差(start与end相减)
     *
     * @param start 开始时间
     * @param end   截止时间
     * @return 时间差
     */
    public static long seconds(Date start, Date end) {
        if (start == null || end == null) {
            return 0L;
        }

        long startTime = TimeUnit.MILLISECONDS.toSeconds(start.getTime());
        long endTime = TimeUnit.MILLISECONDS.toSeconds(end.getTime());

        return Math.abs(endTime - startTime);
    }

    private static int period(Date start, Date end, String formmat) {
        String period = null;
        long startTime = start.getTime();
        long endTime = end.getTime();
        if (startTime <= endTime) {
            period = DurationFormatUtils.formatPeriod(startTime, endTime, formmat);
        } else {
            period = DurationFormatUtils.formatPeriod(endTime, startTime, formmat);
        }
        return Integer.parseInt(period);
    }

    /**
     * 计算当天到24点剩余的秒数
     *
     * @return 到24点剩余秒数
     */
    public static long todaySurplusSecond() {
        Calendar today = Calendar.getInstance();
        return daySurplusSecond(today.getTime());
    }

    /**
     * 计算date到24点剩余的秒数
     *
     * @param date 日期
     * @return 到24点剩余秒数
     */
    public static long daySurplusSecond(Date date) {
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        Calendar nextDay = new GregorianCalendar(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DATE) + 1, 0, 0, 0);
        long time = nextDay.getTimeInMillis() - day.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toSeconds(time);
    }

    @PreDestroy
    public void destroy(){
        MONTH_FORMAT.remove();
        DATE_FORMAT.remove();
        MINUTE_FORMAT.remove();
        TIME_FORMAT.remove();
        SIMPLE_TIME_FORMAT.remove();
        TIME_MINUTE_FORMAT.remove();
        TIME_SECOND_FORMAT.remove();
    }
}
