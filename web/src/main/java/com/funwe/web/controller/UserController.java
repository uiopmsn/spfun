package com.funwe.web.controller;

import com.funwe.core.model.JsonResult;
import com.funwe.dao.entity.SysUser;
import com.funwe.dao.repository.SysUserRepository;
import com.funwe.service.UserService;
import com.funwe.web.helper.SortHelper;
import com.funwe.web.model.PageTable;
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
        PageTable<SysUser> pageTable = new PageTable();
        pageTable.setSuccess(true);
        pageTable.setCurrent(current);
        pageTable.setPageSize(pageSize);
        pageTable.setTotal(pageRole.getTotalElements());
        pageTable.setData(pageRole.getContent());
        return pageTable;
    }

    @PostMapping(value = "api/user/add")
    @ResponseBody
    public JsonResult addRole(@RequestBody Map<String,String> map){
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
}
