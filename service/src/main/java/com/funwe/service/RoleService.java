package com.funwe.service;

import com.funwe.dao.entity.SysRole;
import com.funwe.dao.entity.SysRolePerm;
import com.funwe.dao.repository.system.SysRolePermRepository;
import com.funwe.dao.repository.system.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/3/2 18:48
 */
@Service
public class RoleService {
    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysRolePermRepository rolePermRepository;

    /**
     * 批量停用角色
     * @param roles
     * @throws Exception
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void stopRoles(List<Long> roles) throws Exception{
        for (Long roleId: roles){
            SysRole role = roleRepository.getOne(roleId);
            if (role != null){
                role.setStatus(0);
                role.setUpdatedAt(new Date());
                roleRepository.save(role);
            }
        }
        //TODO redis重置数据
    }

    /**
     * 批量启用角色
     * @param roles
     * @throws Exception
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void resetRoles(List<Long> roles) throws Exception{
        for (Long roleId: roles){
            SysRole role = roleRepository.getOne(roleId);
            if (role != null){
                role.setStatus(1);
                role.setUpdatedAt(new Date());
                roleRepository.save(role);

            }
        }
        //TODO redis重置数据
    }

    /**
     * 更新角色及角色的权限
     * @param ent
     * @param perms
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void updateRole(SysRole ent, List<Long> perms){
        SysRole role = roleRepository.getOne(ent.getId());
        if (role != null){
            role.setRoleCode(ent.getRoleCode());
            role.setRoleName(ent.getRoleName());
            role.setUpdatedAt(new Date());
            roleRepository.save(role);
            //角色权限
            rolePermRepository.deleteByRoleId(role.getId());
            for (Long permId : perms){
                SysRolePerm rolePerm = new SysRolePerm();
                rolePerm.setRoleId(role.getId());
                rolePerm.setPermId(permId);
                rolePermRepository.save(rolePerm);
            }
        }
        //TODO  redis重置数据
    }


}
