package com.taoran.brothers.user.dao;


import com.taoran.brothers.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
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

    @Transactional
    @Modifying
    @Query("update User u set u.password=?1 where u.userId=?2")
    public int updatePasswordByUserId(String password,int userId );

    @Transactional
    @Modifying
    @Query("update User u set u.userName=?1,u.birthday=?2,u.phone=?3,u.address=?4 where u.userId=?5")
    public int editInfo(String userName,String birthday,String phone,String address,int userId);
}