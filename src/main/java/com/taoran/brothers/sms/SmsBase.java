package com.taoran.brothers.sms;

/**
 * Created by taoran
 * date: 2018-07-16 10:22
 */
public class SmsBase {

    // 短信应用SDK AppID
    public static final int appid = 1400113545;

    // 短信应用SDK AppKey
    public static final String appkey = "8a067f4f4747172b26efe575d5f33043";

    // 签名
    public static  String smsSign = "hc兄弟会";

    //签名模板
    public static String smsSignContent = "你的好友{1}今天过生日，请向他/她送上生日祝福！";

    // 短信模板ID，需要在短信应用中申请
    public  static  int templateId = 157551;

}
