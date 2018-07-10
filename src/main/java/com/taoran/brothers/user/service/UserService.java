package com.taoran.brothers.user.service;

import com.taoran.brothers.user.dao.UserDAO;
import com.taoran.brothers.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by taoran on ${date}
 */
@Service
public class UserService {
@Autowired
    private UserDAO userDAO;

public User findByUserName(String username){
    return userDAO.findByUserName(username);
}

    public User findByUserNameAndPassword(String username, String password) {
      return  userDAO.findByUserNameAndPassword(username,password);
    }

    public User save(User user){
       return userDAO.save(user);
    }

    public List<User> findAll(){
        return userDAO.findAll();
    }

    public User findByIndex(String index){
        return userDAO.findByIndex(index);
    }

    public List<User> findByIsShow(String isShow){
        return userDAO.findByIsShow(isShow);
    }
}
