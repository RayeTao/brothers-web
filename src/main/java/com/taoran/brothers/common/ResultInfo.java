package com.taoran.brothers.common;

import java.util.Map;

/**
 * Created by taoran
 * date: 2017-08-08 18:07
 */
public class ResultInfo {

    private  int code;
    private String message;
    private boolean success;
    private Map<String,Object> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
