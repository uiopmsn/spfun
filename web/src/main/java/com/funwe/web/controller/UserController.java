package com.funwe.web.controller;

import com.funwe.core.model.JsonResult;
import com.funwe.dao.entity.SysUser;
import com.funwe.dao.repository.SysUserRepository;
import com.funwe.service.RoleService;
import com.funwe.service.UserService;
import com.funwe.web.helper.SortHelper;
import com.funwe.web.model.PageTable;
import com.funwe.web.model.UserAndRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/3/5 20:20
 */
@RestController
public class UserController {
    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "api/user/list")
    @ResponseBody
    public PageTable getPage(Integer current, Integer pageSize, String sorter,
                                 String userName, String desc, String status)  {
        //查询条件
        Specification<SysUser> specification = new Specification<SysUser>() {
            @Override
            public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNotEmpty(userName)){
                    Predicate likeUserName = cb.like(root.get("userName").as(String.class),"%"+userName+"%");
                    predicates.add(likeUserName);
                }
                if(StringUtils.isNotEmpty(desc)){
                    Predicate likeDesc = cb.like(root.get("desc").as(String.class),"%"+desc+"%");
                    predicates.add(likeDesc);
                }
                if(StringUtils.isNotEmpty(status)){
                    Predicate equalStatus = cb.equal(root.get("status").as(String.class), status);
                    predicates.add(equalStatus);
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
        //查询
        Page<SysUser> pageRole = userRepository.findAll(specification, PageRequest.of(current-1, pageSize, SortHelper.getSort(sorter, "userName")));
        //获取用户角色
        List<UserAndRole> data = new ArrayList<>();
        List<SysUser> users = pageRole.getContent();
        for (SysUser user: users) {
            List<String> roles = roleService.getUserRoles(user.getUserName());
            UserAndRole userAndRole = new UserAndRole();
            userAndRole.setUserName(user.getUserName());
            userAndRole.setUserDesc(user.getUserDesc());
            userAndRole.setStatus(user.getStatus());
            userAndRole.setUserRole(roles);
            data.add(userAndRole);
        }

        PageTable<UserAndRole> pageTable = new PageTable();
        pageTable.setSuccess(true);
        pageTable.setCurrent(current);
        pageTable.setPageSize(pageSize);
        pageTable.setTotal(pageRole.getTotalElements());
        pageTable.setData(data);
        return pageTable;
    }

    @PostMapping(value = "api/user/add")
    @ResponseBody
    public JsonResult addUser(@RequestBody Map<String,String> map){
        String userName = map.get("userName");
        String userDesc = map.get("userDesc");
        try {
            if (userName == null || "".equals(userName)){
                throw new Exception("用户名不能为空");
            }
            if (userDesc == null || "".equals(userDesc)){
                throw new Exception("描述不能为空");
            }
            userService.addUser(userName, userDesc);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.error(e.getMessage());
        }
    }

    @PostMapping(value = "api/user/stop")
    @ResponseBody
    public JsonResult stopUser(@RequestBody Map<String,List<String>> map){
        List<String> ids = map.get("ids");
        try {
            if (ids == null || ids.size() < 1){
                throw new Exception("请选择要停用的用户");
            }
            userService.stopUsers(ids);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.error(e.getMessage());
        }
    }

    @PostMapping(value = "api/user/reset")
    @ResponseBody
    public JsonResult resetUser(@RequestBody Map<String,List<String>> map){
        List<String> ids = map.get("ids");
        try {
            if (ids == null || ids.size() < 1){
                throw new Exception("请选择要启用的用户");
            }
            userService.resetUsers(ids);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.error(e.getMessage());
        }
    }

    @PostMapping(value = "api/user/update")
    @ResponseBody
    public JsonResult updateRole(@RequestBody Map<String,Object> map){
        try {
            String userName = map.get("userName").toString();
            String userDesc = map.get("userDesc").toString();
            List<String> userRole = (List<String>)map.get("userRole");
            List<Long> roles = new ArrayList<>();
            for (String str: userRole){
                roles.add(Long.parseLong(str));
            }

            if ("".equals(userName)){
                throw new Exception("缺少用户名");
            }
            if ("".equals(userDesc)){
                throw new Exception("缺少用户描述");
            }
            userService.updateUser(userName, userDesc, roles);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.error(e.getMessage());
        }
    }

    @PostMapping(value = "api/user/resetPw")
    @ResponseBody
    public JsonResult resetPassword(@RequestBody Map<String,String> map){
        try {
            String userName = map.get("userName");
            userService.resetPassword(userName);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.error(e.getMessage());
        }
    }

}
