package com.gongxiang.zhenxuan.common.utils;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author gongxiang-zhenxuan
 */
public class StringUtil {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空白
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否不为空白
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 去除字符串首尾空格，null安全
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 生成UUID（去除横线）
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 验证手机号格式
     */
    public static boolean isValidPhone(String phone) {
        return isNotBlank(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        return isNotBlank(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证身份证号格式
     */
    public static boolean isValidIdCard(String idCard) {
        return isNotBlank(idCard) && ID_CARD_PATTERN.matcher(idCard).matches();
    }

    /**
     * 手机号脱敏处理
     */
    public static String maskPhone(String phone) {
        if (isBlank(phone) || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 邮箱脱敏处理
     */
    public static String maskEmail(String email) {
        if (isBlank(email) || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 2) {
            return username + "@" + domain;
        }
        
        int maskLength = Math.min(username.length() - 2, 4);
        String masked = username.substring(0, 1) + "*".repeat(maskLength) + username.substring(username.length() - 1);
        return masked + "@" + domain;
    }

    /**
     * 身份证号脱敏处理
     */
    public static String maskIdCard(String idCard) {
        if (isBlank(idCard) || idCard.length() != 18) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(14);
    }

    /**
     * 银行卡号脱敏处理
     */
    public static String maskBankCard(String bankCard) {
        if (isBlank(bankCard) || bankCard.length() < 8) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + "****" + bankCard.substring(bankCard.length() - 4);
    }

    /**
     * 驼峰转下划线
     */
    public static String camelToUnderscore(String camelCase) {
        if (isBlank(camelCase)) {
            return camelCase;
        }
        return camelCase.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    /**
     * 下划线转驼峰
     */
    public static String underscoreToCamel(String underscore) {
        if (isBlank(underscore)) {
            return underscore;
        }
        String[] parts = underscore.split("_");
        StringBuilder result = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].length() > 0) {
                result.append(Character.toUpperCase(parts[i].charAt(0)))
                      .append(parts[i].substring(1));
            }
        }
        return result.toString();
    }

    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        if (isBlank(str)) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String uncapitalize(String str) {
        if (isBlank(str)) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 生成随机字符串
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return result.toString();
    }

    /**
     * 生成随机数字字符串
     */
    public static String generateRandomNumber(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append((int) (Math.random() * 10));
        }
        return result.toString();
    }

    /**
     * 字符串重复
     */
    public static String repeat(String str, int times) {
        if (str == null || times < 0) {
            return "";
        }
        return str.repeat(times);
    }

    /**
     * 左填充
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        return String.valueOf(padChar).repeat(pads) + str;
    }

    /**
     * 右填充
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        return str + String.valueOf(padChar).repeat(pads);
    }
}