package com.funwe.service;

import com.alibaba.fastjson.JSON;
import com.funwe.common.utils.CodecUtil;
import com.funwe.common.utils.JwtUtil;
import com.funwe.dao.entity.SysRole;
import com.funwe.dao.entity.SysUser;
import com.funwe.dao.entity.SysUserRole;
import com.funwe.dao.model.Menu;
import com.funwe.dao.redis.SystemInfoRedis;
import com.funwe.dao.repository.SysUserRepository;
import com.funwe.dao.repository.SysUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/2/11 23:47
 */
@Service
public class UserService {
    @Autowired
    private SystemInfoRedis systemInfoRedis;

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysUserRoleRepository userRoleRepository;

    /**
     * 校验用户名和密码，成功返回TOKEN
     * @param userName
     * @param passWord
     * @return token
     */
    public String checkUser(String userName, String passWord) {
        String token = "";
        //获取Redis中的用户数据
        SysUser user = systemInfoRedis.getByUserName(userName);
        if ((user != null) && (!"".equals(user.getUserName()))){
            //校验密码
            if (CodecUtil.encryptMd5(passWord).equals(user.getPassWord())){
                token = JwtUtil.sign(userName, passWord);
                if (token != null) {
                    return token;
                }
            };
        }
        return token;
    }

    /**
     * 获取用户菜单列表
     * @param userName
     * @return
     */
    public List<Menu> getUserMenus(String userName){
        List<Menu> menus = new ArrayList<>();
        String jsonMenu = systemInfoRedis.getMenusByUserName(userName);
        if (jsonMenu != null && !"".equals(jsonMenu)){
            menus = (List<Menu>)JSON.parse(jsonMenu);
        }
        return menus;
    }

    /**
     * 批量停用用户
     * @param users
     * @throws Exception
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void stopUsers(List<String> users) throws Exception{
        for (String userName: users){
            SysUser user = userRepository.getOne(userName);
            if (user != null){
                user.setStatus(0);
                user.setUpdatedAt(new Date());
                userRepository.save(user);
            }
        }
        //TODO redis重置数据
    }

    /**
     * 批量启用用户
     * @param users
     * @throws Exception
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void resetUsers(List<String> users) throws Exception{
        for (String userName: users){
            SysUser user = userRepository.getOne(userName);
            if (user != null){
                user.setStatus(1);
                user.setUpdatedAt(new Date());
                userRepository.save(user);
            }
        }
        //TODO redis重置数据
    }

    /**
     * 更新用户
     * @param ent
     * @param roles
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void updateUser(SysUser ent, List<Long> roles){
        SysUser user = userRepository.getOne(ent.getUserName());
        if (user != null){
            user.setUserName(ent.getUserName());
            user.setPassWord(ent.getPassWord());
            user.setUpdatedAt(new Date());
            userRepository.save(user);
            //角色
            //TODO
        }
        //TODO  redis重置数据
    }

    /**
     * 添加用户
     * @param userName
     * @param userDesc
     * @throws Exception
     */
    public void addUser(String userName, String userDesc) throws Exception{
        if (userRepository.findById(userName).isPresent()){
            throw new Exception("该用户名已被占用，请更换用户名");
        }
        String defaultPassWord = "123456";
        SysUser user = new SysUser();
        user.setUserName(userName);
        user.setPassWord(CodecUtil.encryptMd5(defaultPassWord));
        user.setUserDesc(userDesc);
        user.setStatus(1);
        user.setUpdatedAt(new Date());
        userRepository.save(user);
        //TODO  redis重置数据
    }

    /**
     * 更新用户及用户的角色
     * @param userName
     * @param userDesc
     * @param roles
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void updateUser(String userName, String userDesc, List<Long> roles){
        SysUser user = userRepository.getOne(userName);
        if (user.getUserName() != null){
            user.setUserDesc(userDesc);
            user.setUpdatedAt(new Date());
            userRepository.save(user);
            //用户角色
            userRoleRepository.deleteByUserName(userName);
            for (Long roleId : roles){
                SysUserRole userRole = new SysUserRole();
                userRole.setUserName(userName);
                userRole.setRoleId(roleId);
                userRoleRepository.save(userRole);
            }
        }
        //TODO  redis重置数据
    }

    /**
     * 重置用户密码
     * @param userName
     */
    public void resetPassword(String userName){
        SysUser user = userRepository.getOne(userName);
        if (user.getUserName() != null){
            String defaultPassword = "123456";
            String password = CodecUtil.encryptMd5(defaultPassword);
            user.setPassWord(password);
            user.setUpdatedAt(new Date());
            userRepository.save(user);
        }
        //TODO  redis重置数据
    }
}
