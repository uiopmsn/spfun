package com.funwe.dao.redis;

import com.funwe.dao.entity.SysPerm;
import com.funwe.dao.entity.SysRole;
import com.funwe.dao.entity.SysUser;
import com.funwe.dao.entity.SysUserCurrentDatasource;
import com.funwe.dao.model.Menu;
import com.funwe.dao.repository.SysPermRepository;
import com.funwe.dao.repository.SysRoleRepository;
import com.funwe.dao.repository.SysUserDataSourceRepository;
import com.funwe.dao.repository.SysUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Component
public class InitRedisData implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(InitRedisData.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysUserDataSourceRepository sysUserDataSourceRepository;

    @Autowired
    private SysPermRepository sysPermRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("开始初始化Redis数据.");
        try {
            List<SysUser> userList = userRepository.findAll();
            for (SysUser user: userList) {
                String userName = user.getUserName();
                //用户账号密码
                stringRedisTemplate.opsForValue().set(RedisConstant.USER_TOKEN + userName, user.getPassWord(),
                        RedisConstant.TIME_LONG, RedisConstant.TIME_UNIT);
                //用户拥有的角色
                List<SysRole> sysRoles = roleRepository.findRolesOfUser(userName);
                String roles = changeToString(sysRoles);
                stringRedisTemplate.opsForValue().set(RedisConstant.USER_ROLES + userName, roles,
                        RedisConstant.TIME_LONG, RedisConstant.TIME_UNIT);
                //用户的菜单列表
                String menus = getUserMenus(userName);
                stringRedisTemplate.opsForValue().set(RedisConstant.USER_MENUS + userName, menus,
                        RedisConstant.TIME_LONG, RedisConstant.TIME_UNIT);
            }

            //用户当前使用的数据库
            List<SysUserCurrentDatasource> userDataSources = sysUserDataSourceRepository.findAll();
            for (SysUserCurrentDatasource userDataSource: userDataSources) {
                stringRedisTemplate.opsForValue().set(RedisConstant.USER_DATASOURCE + userDataSource.getUserName(), userDataSource.getDataSourceId(),
                        RedisConstant.TIME_LONG, RedisConstant.TIME_UNIT);
            }
            //权限所需的角色列表
            List<SysPerm> perms = sysPermRepository.findAll();
            for (SysPerm perm: perms) {
                List<SysRole> sysRoles = roleRepository.findRolesOfPerm(perm.getId());
                String roles = changeToString(sysRoles);
                stringRedisTemplate.opsForValue().set(RedisConstant.PERM_ROLE + perm.getPermCode(), roles,
                        RedisConstant.TIME_LONG, RedisConstant.TIME_UNIT);

            }

        }catch (Exception e){
            logger.error("初始化Redis数据失败：" + e.getMessage());
            throw e;
        }
        logger.info("结束初始化Redis数据.");
    }


    private String changeToString(List<SysRole> sysRoles){
        String roles = "";
        if (sysRoles != null && sysRoles.size()>0){
            StringBuilder sb = new StringBuilder();
            for (SysRole role: sysRoles) {
                sb.append(role.getRoleCode());
                sb.append(",");
            }
            roles = sb.toString().substring(0, sb.toString().length()-1);
        }
        return roles;
    }

    /**
     * 根据用户名获取菜单列表
     * @param userName
     * @return String (json)
     */
    private String getUserMenus(String userName){
        String result = "[]";
        List<SysPerm> sysPerms = sysPermRepository.findPermOfUser(userName);
        if (sysPerms != null && sysPerms.size()>0){
            List<Menu> menus = new ArrayList<>();
            for(SysPerm perm: sysPerms){
                if(perm.getPid() == -1){
                    Menu menu = new Menu();
                    menu.setPath(perm.getPermCode());
                    menu.setName(perm.getPermName());
                    menu.setIcon(perm.getIcon());
                    addChildMenu(menu, perm, sysPerms);
                    menus.add(menu);
                }
            }
            result = JSON.toJSONString(menus);
        }
        return result;
    }

    public void addChildMenu(Menu parent, SysPerm sysPerm, List<SysPerm> list) {
        List<Menu> childList = new ArrayList<>();
        parent.setChildren(childList);
        for (SysPerm child : list) {
            if (child.getPid() == sysPerm.getId()) {
                Menu menu = new Menu();
                menu.setPath(child.getPermCode());
                menu.setName(child.getPermName());
                menu.setIcon(child.getIcon());
                childList.add(menu);
                // 递归调用
                addChildMenu(menu, child, list);
            }
        }
    }

}
