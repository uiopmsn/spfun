package com.funwe.web.model;

import com.funwe.dao.entity.SysRole;


import java.util.List;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/2/27 18:53
 */
public class RoleTable {
    private boolean success;
    private long total;
    private int pageSize;
    private int current;
    private List<SysRole> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<SysRole> getData() {
        return data;
    }

    public void setData(List<SysRole> data) {
        this.data = data;
    }
}
