package com.taoran.brothers.user.dao;


import com.taoran.brothers.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by taoran
 * date: 2017-08-08 13:38
 */
public interface UserDAO extends JpaRepository<User,Integer> {

    public  User findByUserName(String username) ;

    public  User findByUserNameAndPassword(String username, String password);

    public List<User> findAll();

    public User findByIndex(String index);

    public List<User> findByIsShow(String isShow);
}
