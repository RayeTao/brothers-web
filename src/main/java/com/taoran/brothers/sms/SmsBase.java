package com.taoran.brothers.sms;

/**
 * Created by taoran
 * date: 2018-07-16 10:22
 */
public class SmsBase {

    // 短信应用SDK AppID
    public static final int appid = 1400113545; // 1400开头

    // 短信应用SDK AppKey
    public static final String appkey = "8a067f4f4747172b26efe575d5f3304";

    // 签名
    String smsSign = "腾讯云"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID

}
