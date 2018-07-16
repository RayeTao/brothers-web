package com.taoran.brothers.sms;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.json.JSONException;
import com.github.qcloudsms.httpclient.HTTPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
     * 单发短信
     * @param phone
     */
    @RequestMapping(value = "/sendSingle",method = RequestMethod.GET)
    public void sendSmsSingle(String phone){
        try{
            SmsSingleSender smsSingleSender = new SmsSingleSender(SmsBase.appid,SmsBase.appkey);
            SmsSingleSenderResult result = smsSingleSender.send(0,"86","18771041578","短信测试","","");
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


}
