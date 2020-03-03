package com.funwe.web.controller;

import com.funwe.core.model.JsonResult;
import com.funwe.service.PermService;
import com.funwe.service.model.PermTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/3/2 19:07
 */
@RestController
public class PermController {
    @Autowired
    private PermService permService;

    @GetMapping(value = "api/perm/getAllPerm")
    @ResponseBody
    public JsonResult getAllPerm(){
        List<PermTree> permTrees = permService.getAllPermTree();
        return JsonResult.success("成功", permTrees);
    }

    @GetMapping(value = "api/perm/getRolePerm")
    @ResponseBody
    public JsonResult getRolePerm(Long roleId){
        if (roleId == null){
            return JsonResult.error("缺少当前操作角色ID，获取角色已有权限失败");
        }
        List<Long> perms = permService.getRolePerm(roleId);
        List<String> data = new ArrayList<>();
        for (Long perm: perms){
            data.add(perm.toString());
        }
        return JsonResult.success("成功", data);
    }

}
