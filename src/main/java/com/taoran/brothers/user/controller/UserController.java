package com.taoran.brothers.user.controller;

import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.user.pojo.User;
import com.taoran.brothers.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResultInfo getUserList() throws Exception{
        return userService.getUserList();
    }

    /**
     * 修改用户信息
     * @param userId
     * @param userName
     * @param birthday
     * @param phone
     * @param address
     * @return
     */
    @RequestMapping(value = "/editInfo",method = RequestMethod.GET)
    public ResultInfo editInfo(@RequestParam int userId,@RequestParam String userName,@RequestParam String birthday,
                               @RequestParam String phone,@RequestParam String address){
        return userService.editInfo(userId,userName,birthday,phone,address);
    }
}
