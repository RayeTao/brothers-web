package com.taoran.brothers.sms;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.taoran.brothers.user.dao.UserDAO;
import com.taoran.brothers.user.pojo.User;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;

/**
 * Created by taoran
 * date: 2018-07-17 17:26
 */
public class SmsSendUtil {

    @Autowired
    UserDAO userDAO;
    /**
     * 指定模板单发短信
     * @param phone
     * @param userName
     */
    public  void sendSmsSingle(String phone,String userName){
        String[] params = {userName};
        try{
            SmsSingleSender smsSingleSender = new SmsSingleSender(SmsBase.appid,SmsBase.appkey);
            SmsSingleSenderResult result = smsSingleSender.sendWithParam("86", phone,
                    SmsBase.templateId, params, SmsBase.smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
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
     * @param userName
     */
    public void sendSmsMulti(String userName){
        List<User> users = userDAO.findByIsShow("1");
        if(users != null){
            String[] phones = new String[users.size()];
            String[] params = {userName};
            for(int i=0; i<users.size(); i++){
               phones[i] = users.get(i).getPhone();
            }
            try {
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
}
