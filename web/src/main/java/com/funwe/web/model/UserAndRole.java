package com.funwe.web.model;

import java.util.List;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/3/13 0:00
 */
public class UserAndRole {
    private String userName;
    private String userDesc;
    private int status;
    private List<String> userRole;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getUserRole() {
        return userRole;
    }

    public void setUserRole(List<String> userRole) {
        this.userRole = userRole;
    }
}
