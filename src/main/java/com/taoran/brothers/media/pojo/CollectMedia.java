package com.taoran.brothers.media.pojo;

import com.sun.javafx.beans.IDProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by taoran
 * date: 2018-07-19 14:49
 */
@Entity
@Getter
@Setter
@ToString
public class CollectMedia {

    @Id
    @GeneratedValue
    private int collectId;
    private int userId;
    private int mediaId;
    private Date createTime;

}
