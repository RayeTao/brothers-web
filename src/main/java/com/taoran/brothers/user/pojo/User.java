package com.taoran.brothers.user.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by taoran
 * date: 2018-06-29 20:37
 */
@Entity
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue
    private  int userId;
    private  String userName;
    private String password;
    private Date createTime;
    private String remark;
    private String index;
    private String isShow;
    private String birthday;
    private String phone;
    private String gender;
    private String address;

}
