package com.funwe.dao.model;

import java.util.List;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/2/20 18:20
 */
public class Menu {
    private String path;
    private String name;
    private String icon;
    private List<Menu> children;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}
