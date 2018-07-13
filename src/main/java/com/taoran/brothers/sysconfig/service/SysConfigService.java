package com.taoran.brothers.sysconfig.service;

import com.taoran.brothers.common.ResultInfo;
import com.taoran.brothers.sysconfig.dao.SysConfigDAO;
import com.taoran.brothers.sysconfig.pojo.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by taoran
 * date: 2018-07-11 20:17
 */
@Service
public class SysConfigService {
    @Autowired
    SysConfigDAO sysConfigDAO;

    public ResultInfo findByConfigName(String configName){
        ResultInfo resultInfo = new ResultInfo();
        SysConfig sysConfig =  sysConfigDAO.findByConfigName(configName);
        if(sysConfig != null){
            resultInfo.setSuccess(true);
            resultInfo.setData(sysConfig);
        }
        return resultInfo;

    }

}
