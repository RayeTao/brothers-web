package com.taoran.brothers.user.service;

import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.user.dao.UserDAO;
import com.taoran.brothers.user.pojo.User;
import com.taoran.brothers.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by taoran on ${date}
 */
@Service
public class UserService {
@Autowired
    private UserDAO userDAO;

public ResultInfo login(String username,String password) throws Exception{
    ResultInfo result = new ResultInfo();
    User user = userDAO.findByUserName(username);
    if(user==null){
        result.setCode(-1);
        result.setSuccess(false);
        result.setMessage("用户不存在");
    }else{
        user = userDAO.findByUserNameAndPassword(username,MD5Util.MD5LowerCase(password));
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
public ResultInfo resetPassword( String username, String oldPassword,String newPassword ) throws Exception{
    ResultInfo result = new ResultInfo();
    User user = userDAO.findByUserNameAndPassword(username,MD5Util.MD5LowerCase(oldPassword));
    if(user == null ){
        result.setCode(-1);
        result.setSuccess(false);
        result.setMessage("原始密码输入错误");
    }else{
        int newUser = userDAO.updatePasswordByUserId(MD5Util.MD5LowerCase(newPassword),user.getUserId());
        if(newUser != 0){
            result.setCode(0);
            result.setSuccess(false);
            result.setMessage("密码修改成功");
            Map<String,Object> userInfo = new HashMap<String,Object>();
            userInfo.put("data",newUser);
            result.setData(userInfo);
        }
    }
    return result;
}

public ResultInfo getUserList() throws Exception{
    ResultInfo resultInfo = new ResultInfo();
    //获取用户列表
    List<User> userList = userDAO.findByIsShow("1");
    if(userList!=null && userList.size()>0){
        Map<String , Object> resultMap = new HashMap<String , Object>();
        resultMap.put("resultList",userList);
        resultInfo.setCode(0);
        resultInfo.setData(resultMap);
        resultInfo.setSuccess(true);
    }
    return resultInfo;
}

    public ResultInfo editInfo(int userId, String userName, String birthday, String phone, String address) {
    ResultInfo resultInfo = new ResultInfo();
    int result = userDAO.editInfo(userName,birthday,phone,address,userId);
    if(result != 0){
        resultInfo.setSuccess(true);
        resultInfo.setMessage("修改成功");
    }
    return  resultInfo;
    }
}
