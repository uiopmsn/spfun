package com.funwe.service.system;

import com.alibaba.fastjson.JSON;
import com.funwe.common.utils.CodecUtil;
import com.funwe.common.utils.JwtUtil;
import com.funwe.dao.entity.SysUser;
import com.funwe.dao.model.Menu;
import com.funwe.dao.redis.SystemInfoRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
