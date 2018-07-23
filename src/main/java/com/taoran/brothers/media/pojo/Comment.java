package com.taoran.brothers.media.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;


/**
 * Created by taoran
 * date: 2018-07-23 16:12
 */
@Entity
@Getter
@Setter
@ToString
public class Comment {
    @Id
    @GeneratedValue
    private int commentId;
    private int userId;
    private int mediaId;
    private Date createTime;
    private String content;

    @Transient
    private String userName;
}
