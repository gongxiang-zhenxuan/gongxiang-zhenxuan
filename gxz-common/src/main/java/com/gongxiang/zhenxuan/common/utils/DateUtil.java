package com.gongxiang.zhenxuan.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 日期时间工具类
 *
 * @author gongxiang-zhenxuan
 */
public class DateUtil {

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";
    public static final String PATTERN_DATE_COMPACT = "yyyyMMdd";

    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATETIME);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATE);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_TIME);

    /**
     * 获取当前时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * 格式化日期时间为字符串
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DATETIME_FORMATTER);
    }

    /**
     * 格式化日期为字符串
     */
    public static String format(LocalDate date) {
        return format(date, DATE_FORMATTER);
    }

    /**
     * 格式化时间为字符串
     */
    public static String format(LocalTime time) {
        return format(time, TIME_FORMATTER);
    }

    /**
     * 使用指定格式格式化日期时间
     */
    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime != null ? dateTime.format(formatter) : null;
    }

    /**
     * 使用指定格式格式化日期
     */
    public static String format(LocalDate date, DateTimeFormatter formatter) {
        return date != null ? date.format(formatter) : null;
    }

    /**
     * 使用指定格式格式化时间
     */
    public static String format(LocalTime time, DateTimeFormatter formatter) {
        return time != null ? time.format(formatter) : null;
    }

    /**
     * 使用指定模式格式化日期时间
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        return format(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析字符串为日期时间
     */
    public static LocalDateTime parse(String dateTimeString) {
        return parse(dateTimeString, DATETIME_FORMATTER);
    }

    /**
     * 解析字符串为日期
     */
    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    /**
     * 解析字符串为时间
     */
    public static LocalTime parseTime(String timeString) {
        return LocalTime.parse(timeString, TIME_FORMATTER);
    }

    /**
     * 使用指定格式解析字符串为日期时间
     */
    public static LocalDateTime parse(String dateTimeString, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * 使用指定模式解析字符串为日期时间
     */
    public static LocalDateTime parse(String dateTimeString, String pattern) {
        return parse(dateTimeString, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDateTime 转 Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate 转 Date
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转 LocalDateTime
     */
    public static LocalDateTime fromDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Date 转 LocalDate
     */
    public static LocalDate fromDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 获取时间戳（毫秒）
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取时间戳（秒）
     */
    public static long getTimestampSecond() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 时间戳转LocalDateTime
     */
    public static LocalDateTime fromTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转时间戳
     */
    public static long toTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 计算两个时间的差值（天数）
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 计算两个时间的差值（小时数）
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个时间的差值（分钟数）
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个时间的差值（秒数）
     */
    public static long secondsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.SECONDS.between(startDateTime, endDateTime);
    }

    /**
     * 获取一天的开始时间（00:00:00）
     */
    public static LocalDateTime getStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    /**
     * 获取一天的结束时间（23:59:59）
     */
    public static LocalDateTime getEndOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    /**
     * 判断时间是否在指定范围内
     */
    public static boolean isBetween(LocalDateTime target, LocalDateTime start, LocalDateTime end) {
        return !target.isBefore(start) && !target.isAfter(end);
    }

    /**
     * 生成订单号（基于时间戳）
     */
    public static String generateOrderNo(String prefix) {
        String timestamp = format(now(), DateTimeFormatter.ofPattern(PATTERN_DATETIME_COMPACT));
        return prefix + timestamp + String.format("%03d", (int) (Math.random() * 1000));
    }
}