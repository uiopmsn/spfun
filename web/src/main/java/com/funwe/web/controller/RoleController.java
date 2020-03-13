package com.funwe.web.controller;

import com.funwe.core.model.JsonResult;
import com.funwe.dao.entity.SysRole;
import com.funwe.dao.repository.SysRoleRepository;
import com.funwe.service.RoleService;
import com.funwe.web.helper.SortHelper;
import com.funwe.web.model.KeyValue;
import com.funwe.web.model.PageTable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/2/27 17:41
 */
@RestController
public class RoleController {
    private Logger log = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "api/role/list")
    @ResponseBody
    public PageTable getPageRole(Integer current, Integer pageSize, String sorter,
                                 String roleCode, String roleName, String status, String updatedAt)  {
        //查询条件
        Specification<SysRole> specification = new Specification<SysRole>() {
            @Override
            public Predicate toPredicate(Root<SysRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNotEmpty(roleCode)){
                    Predicate likeRoleCode = cb.like(root.get("roleCode").as(String.class),"%"+roleCode+"%");
                    predicates.add(likeRoleCode);
                }
                if(StringUtils.isNotEmpty(roleName)){
                    Predicate likeRoleName = cb.like(root.get("roleName").as(String.class),"%"+roleName+"%");
                    predicates.add(likeRoleName);
                }
                if(StringUtils.isNotEmpty(status)){
                    Predicate equalStatus = cb.equal(root.get("status").as(String.class), status);
                    predicates.add(equalStatus);
                }
                if(StringUtils.isNotEmpty(updatedAt)){
                    Date date = null;
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = format.parse(updatedAt);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Predicate equalUpdatedAt = cb.equal(root.get("updatedAt").as(Date.class), date);
                    predicates.add(equalUpdatedAt);
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
        //查询
        Page<SysRole> pageRole = roleRepository.findAll(specification, PageRequest.of(current-1, pageSize, SortHelper.getSort(sorter, "roleCode")));
        PageTable<SysRole> pageTable = new PageTable();
        pageTable.setSuccess(true);
        pageTable.setCurrent(current);
        pageTable.setPageSize(pageSize);
        pageTable.setTotal(pageRole.getTotalElements());
        pageTable.setData(pageRole.getContent());
        return pageTable;
    }

    @PostMapping(value = "api/role/add")
    @ResponseBody
    public JsonResult addRole(@RequestBody Map<String,String> map){
        String roleCode = map.get("roleCode");
        String roleName = map.get("roleName");
        try {
            if (roleCode == null || "".equals(roleCode)){
                throw new Exception("角色编码不能为空");
            }
            if (roleName == null || "".equals(roleName)){
                throw new Exception("角色描述不能为空");
            }
            SysRole role = new SysRole();
            role.setRoleCode(roleCode);
            role.setRoleName(roleName);
            role.setStatus(1);
            role.setUpdatedAt(new Date());
            roleRepository.save(role);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.error(e.getMessage());
        }
    }

    @PostMapping(value = "api/role/stop")
    @ResponseBody
    public JsonResult stopRole(@RequestBody Map<String,List<Long>> map){
        List<Long> ids = map.get("ids");
        try {
            if (ids == null || ids.size() < 1){
                throw new Exception("请选择要停用的角色");
            }
            roleService.stopRoles(ids);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.error(e.getMessage());
        }
    }

    @PostMapping(value = "api/role/reset")
    @ResponseBody
    public JsonResult resetRole(@RequestBody Map<String,List<Long>> map){
        List<Long> ids = map.get("ids");
        try {
            if (ids == null || ids.size() < 1){
                throw new Exception("请选择要启用的角色");
            }
            roleService.resetRoles(ids);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.error(e.getMessage());
        }
    }

    @PostMapping(value = "api/role/update")
    @ResponseBody
    public JsonResult updateRole(@RequestBody Map<String,Object> map){
        try {
            long id = Long.parseLong(map.get("id").toString());
            String roleCode = map.get("roleCode").toString();
            String roleName = map.get("roleName").toString();
            List<String> strPerms = (List<String>)map.get("perms");
            List<Long> perms = new ArrayList<>();
            for (String str: strPerms){
                perms.add(Long.parseLong(str));
            }
            if ("".equals(id)){
                throw new Exception("缺少id");
            }
            if ("".equals(roleCode)){
                throw new Exception("缺少角色编码");
            }
            if ("".equals(roleName)){
                throw new Exception("缺少角色描述");
            }
            SysRole role = new SysRole();
            role.setId(id);
            role.setRoleCode(roleCode);
            role.setRoleName(roleName);
            roleService.updateRole(role, perms);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.error(e.getMessage());
        }
    }

    @GetMapping(value = "api/role/getAllUsedRoles")
    @ResponseBody
    public JsonResult getAllUsedRoles(){
        try {
            List<KeyValue> data = new ArrayList<>();
            List<SysRole> roles = roleService.getAllUsedRole();
            for (SysRole role: roles){
                KeyValue keyValue = new KeyValue();
                keyValue.setKey(String.valueOf(role.getId()));
                keyValue.setValue(role.getRoleName());
                data.add(keyValue);
            }
            return JsonResult.success("成功", data);
        }catch (Exception e){
            return JsonResult.error(e.getMessage());
        }
    }


}
