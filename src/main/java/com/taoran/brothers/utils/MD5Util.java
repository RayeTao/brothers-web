package com.taoran.brothers.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by taoran
 * date: 2018-07-16 19:53
 */
public class MD5Util {

    /**
     * 对字符串md5加密
     *
     * @param key 传入要加密的字符串
     * @return MD5加密后的字符串(小写+字母)
     */
    public static String MD5LowerCase(String key) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 计算md5函数
            messageDigest.update(key.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对字符串md5加密(大写+字母)
     *
     * @param key 传入要加密的字符串
     * @return MD5加密后的字符串(大写+字母)
     */
    public static String MD5UpperCase(String key) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] input = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            messageDigest.update(input);
            // 获得密文
            byte[] md = messageDigest.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}