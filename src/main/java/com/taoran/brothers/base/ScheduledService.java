package com.taoran.brothers.base;

import com.taoran.brothers.sms.SmsSendUtil;
import com.taoran.brothers.user.dao.UserDAO;
import com.taoran.brothers.user.pojo.User;
import com.taoran.brothers.utils.CalendarUtil;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by taoran
 * date: 2018-07-18 10:08
 * 定时任务
 */
@Slf4j
@Component
public class ScheduledService {

    @Autowired
    UserDAO userDAO;

    /**
     * 检查当天是否有人过生日，定时发送短信
     */
        @Scheduled(cron = "0 0 8 * * ?")
    public void checkBirth(){
        Date date = new Date();
        //获取当年的年份
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String year = simpleDateFormat.format(date);

        //获取当天的日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String todayDate = sdf.format(date);

        //判断当年是否是闰年
        boolean leapMonthFlag = Integer.parseInt(year)%4 == 0 ? true : false;

        //获取当天阴历过生日的用户
       try {
            List<User> list = userDAO.findByIsShow("1");
            if(list != null){
                for(User user : list){
                    String birthday = year + user.getBirthday().replace("-","");
                    if(CalendarUtil.lunarToSolar(birthday,leapMonthFlag).equals(todayDate)){
                        SmsSendUtil smsSendUtil = new SmsSendUtil();
                        smsSendUtil.sendSmsMulti(user.getUserName());
                   }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
