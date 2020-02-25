package com.funwe.dao.redis;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
public interface RedisConstant {
    /**
     * 缓存保存时间
     */
    long TIME_LONG = 30;
    /**
     * 时间类型
     */
    TimeUnit TIME_UNIT = TimeUnit.DAYS;
    /**
     * 用户-密码
     */
    String USER_TOKEN = "userToken-";
    /**
     * 用户-角色
     */
    String USER_ROLES = "userRoles-";
    /**
     * 用户-数据源ID
     */
    String USER_DATASOURCE = "userDatasource-";
    /**
     * 权限-角色
     */
    String PERM_ROLE = "permRole-";

    /**
     * 用户-菜单
     */
    String USER_MENUS = "userMenu-";

}
