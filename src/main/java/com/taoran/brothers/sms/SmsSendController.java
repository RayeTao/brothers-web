package com.taoran.brothers.sms;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.taoran.brothers.user.pojo.User;
import org.json.JSONException;
import com.github.qcloudsms.httpclient.HTTPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by taoran
 * date: 2018-07-16 10:54
 */
@RestController
@RequestMapping(value = "/sms")
public class SmsSendController {
    private static final Logger logger = LoggerFactory.getLogger(SmsSendController.class);

    /**
     * 指定模板单发短信
     * @param phone
     */
    @RequestMapping(value = "/sendSingle",method = RequestMethod.GET)
    public void sendSmsSingle(@RequestParam String phone,@RequestParam String userName){
        try{
            String[] params = {userName};
            SmsSingleSender smsSingleSender = new SmsSingleSender(SmsBase.appid,SmsBase.appkey);
            SmsSingleSenderResult result = smsSingleSender.sendWithParam("86", phone,
                    SmsBase.templateId, params, SmsBase.smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            logger.info(result.toString());
        }catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }

    }

    /**
     * 指定模板群发短信
     */
    @RequestMapping(value = "/sendMutli",method = RequestMethod.GET)
    public void sendSmsMulti(@RequestParam String[] phones,@RequestParam String userName){
        try {
            String[] params = {userName};
            SmsMultiSender smsMultiSender = new SmsMultiSender(SmsBase.appid, SmsBase.appkey);
            SmsMultiSenderResult result =  smsMultiSender.sendWithParam("86", phones,
                    SmsBase.templateId, params, SmsBase.smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.print(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
    }
}
