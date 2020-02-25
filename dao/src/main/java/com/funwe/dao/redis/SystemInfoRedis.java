package com.funwe.dao.redis;

import com.funwe.dao.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Administrator
 */
@Repository
public class SystemInfoRedis {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取数据库用户对象（用户名和密码)
     * @param userName
     * @return
     */
    public SysUser getByUserName(String userName){
        SysUser sysUser = new SysUser();
        String password = stringRedisTemplate.opsForValue().get(RedisConstant.USER_TOKEN + userName);
        if (password != null && !password.isEmpty()){
            sysUser.setUserName(userName);
            sysUser.setPassWord(password);
        }
        return sysUser;
    }

    /**
     * 获取用户所使用的数据源
     * @param userName
     * @return dsName
     */
    public String getDataSourceByUserName(String userName){
        return stringRedisTemplate.opsForValue().get(RedisConstant.USER_DATASOURCE + userName);
    }

    /**
     * 根据用户名获取所拥有的角色
     * @param userName
     * @return admin,user,...
     */
    public String getRolesByUserName(String userName){
        String roles = stringRedisTemplate.opsForValue().get(RedisConstant.USER_ROLES + userName);
        return roles == null ? "": roles;
    }

    /**
     * 根据用户名获取所拥有的角色列表
     * @param userName
     * @return List<String>
     */
    public List<String> getRoleListByUserName(String userName){
        List<String> roleList = new ArrayList<>();
        String roles = stringRedisTemplate.opsForValue().get(RedisConstant.USER_ROLES + userName);
        if (roles != null && !"".equals(roles)) {
            String[] roleArray = roles.split(",");
            roleList = Arrays.asList(roleArray);
        }
        return roleList;
    }


    /**
     * 根据权限，获取需要的角色信息
     * @param permCode
     * @return admin,user,...
     */
    public String getRolesByPermCode(String permCode){
        String roles = stringRedisTemplate.opsForValue().get(RedisConstant.PERM_ROLE + permCode);
        return roles == null ? "": roles;
    }

    /**
     * 根据权限，获取需要的角色列表
     * @param permCode
     * @return List<String>
     */
    public List<String> getRoleListByPermCode(String permCode){
        List<String> roleList = new ArrayList<String>();
        String roles = stringRedisTemplate.opsForValue().get(RedisConstant.PERM_ROLE + permCode);
        if (roles != null && !"".equals(roles)){
            String[] roleArray = roles.split(",");
            roleList = Arrays.asList(roleArray);
        }
        return roleList;
    }

    /**
     * 根据用户名获取菜单
     * @param userName
     * @return []
     */
    public String getMenusByUserName(String userName){
        String menus = stringRedisTemplate.opsForValue().get(RedisConstant.USER_MENUS + userName);
        return menus == null ? "[]": menus;
    }
}
