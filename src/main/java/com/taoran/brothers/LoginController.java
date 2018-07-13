package com.taoran.brothers;

import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.user.pojo.User;
import com.taoran.brothers.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
public class LoginController {
   private  static  final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/doLogin",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo doLogin(@RequestParam  String username, @RequestParam  String password) throws Exception{
        return userService.login(username,password);
    }

    /**
     * 修改密码
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value="/resetPassword",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo resetPsw(@RequestParam String username, @RequestParam String oldPassword, @RequestParam String newPassword )throws Exception{
     return userService.resetPassword(username,oldPassword,newPassword);
    }
}
