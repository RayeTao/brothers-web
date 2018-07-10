package com.taoran.brothers.user.controller;

import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.user.pojo.User;
import com.taoran.brothers.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by taoran
 * date: 2018-07-05 13:41
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private  static  final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     * @return
     */
    @RequestMapping(value = "/getUserList",method = RequestMethod.GET)
    public ResultInfo getUserList(){
        ResultInfo resultInfo = new ResultInfo();
        //获取用户列表
        List<User> userList = userService.findByIsShow("1");
        if(userList!=null && userList.size()>0){
            Map<String , Object> resultMap = new HashMap<String , Object>();
            resultMap.put("resultList",userList);
            resultInfo.setCode(0);
            resultInfo.setData(resultMap);
            resultInfo.setSuccess(true);
        }
        return resultInfo;
    }
}
