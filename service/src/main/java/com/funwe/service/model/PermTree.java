package com.funwe.service.model;

import java.util.List;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/3/2 18:59
 */
public class PermTree {
    private String key;
    private String title;
    private List<PermTree> children;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<PermTree> getChildren() {
        return children;
    }

    public void setChildren(List<PermTree> children) {
        this.children = children;
    }
}
