package com.funwe.dao.entity;

import javax.persistence.*;

/**
 * @author Administrator
 */
@Entity
@Table(name = "sys_perm")
public class SysPerm{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String permCode;
    private String permName;
    private String icon;
    private long pid;
    private String type;
    private Integer orderBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPermCode() {
        return permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
