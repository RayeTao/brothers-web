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
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/doLogin",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo doLogin(@RequestParam("username") String username, @RequestParam("password") String password,
                              HttpServletRequest request, HttpServletResponse response) throws Exception{
       // response.setHeader("Access-Control-Allow-Origin", "*");
        ResultInfo result = new ResultInfo();
       User user = userService.findByUserName(username);
       logger.info("用户名为："+username+",长度："+username.length());
       if(user==null){
           result.setCode(-1);
           result.setSuccess(false);
           result.setMessage("用户不存在");
       }else{
           user = userService.findByUserNameAndPassword(username,password);
           if(user==null){
               result.setCode(1);
               result.setSuccess(false);
               result.setMessage("用户密码错误");
           }else{
               result.setCode(0);
               result.setSuccess(true);
               result.setMessage("用户登录成功");
               Map<String,Object> userInfo = new HashMap<String,Object>();
               userInfo.put("data",user);
               result.setData(userInfo);
           }
       }
        return result;
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
    public ResultInfo resetPsw(@RequestParam("username") String username, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword ){
        ResultInfo result = new ResultInfo();
        User user = userService.findByUserNameAndPassword(username,oldPassword);
        if(user == null ){
            result.setCode(-1);
            result.setSuccess(false);
            result.setMessage("原始密码输入错误");
        }else{
            User newUser = new User();
            user.setUserName(username);
            user.setPassword(newPassword);
            User newUser2 = userService.save(user);
            if(newUser2 != null){
                result.setCode(0);
                result.setSuccess(false);
                result.setMessage("密码修改成功");
                Map<String,Object> userInfo = new HashMap<String,Object>();
                userInfo.put("data",newUser2);
                result.setData(userInfo);
            }
        }
        return result;
    }
}
