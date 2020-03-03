package com.funwe.web.controller;

import com.funwe.core.model.CurrentUser;
import com.funwe.core.model.JsonResult;
import com.funwe.dao.model.Menu;
import com.funwe.service.UserService;
import com.funwe.web.model.LoginData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "api/login/account")
    @ResponseBody
    public JsonResult login (@RequestBody Map<String,String> map){
        String userName = map.get("userName");
        String passWord = map.get("passWord");
        String type = map.get("type");
        //身份验证
        String token = userService.checkUser(userName,passWord);
        LoginData data = new LoginData();
        data.setToken(token);
        data.setType(type);

        if (!"".equals(token)) {
            return JsonResult.success("成功", data);
        }
        return JsonResult.error("用户名及密码错误");
    }

    @GetMapping(value = "api/currentMenu")
    @ResponseBody
    public JsonResult getUserMenus(HttpServletRequest request){
        CurrentUser currentUser = (CurrentUser)request.getAttribute("currentUser");
        List<Menu> menus = userService.getUserMenus(currentUser.getUserName());
        return JsonResult.success("成功", menus);
    }

}

