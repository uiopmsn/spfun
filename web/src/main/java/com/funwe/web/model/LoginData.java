package com.funwe.web.model;



/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/2/20 11:08
 */
public class LoginData {
    private String token;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
