package com.funwe.service;

import com.funwe.dao.entity.SysPerm;
import com.funwe.dao.entity.SysRolePerm;
import com.funwe.dao.repository.system.SysPermRepository;
import com.funwe.dao.repository.system.SysRolePermRepository;
import com.funwe.service.model.PermTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/3/2 18:51
 */
@Service
public class PermService {
    @Autowired
    private SysPermRepository permRepository;

    @Autowired
    private SysRolePermRepository rolePermRepository;

    public List<SysPerm> getAllPerm(){
        return permRepository.findAll();
    }

    public List<PermTree> getAllPermTree(){
        List<SysPerm> sysPerms = getAllPerm();
        List<PermTree> permTrees = new ArrayList<>();
        for(SysPerm perm: sysPerms){
            if(perm.getPid() == -1){
                PermTree permTree = new PermTree();
                permTree.setKey(String.valueOf(perm.getId()));
                permTree.setTitle(perm.getPermName());
                addChildMenu(permTree, perm, sysPerms);
                permTrees.add(permTree);
            }
        }
        return permTrees;
    }

    public void addChildMenu(PermTree parent, SysPerm sysPerm, List<SysPerm> list) {
        List<PermTree> childList = new ArrayList<>();
        parent.setChildren(childList);
        for (SysPerm child : list) {
            if (child.getPid() == sysPerm.getId()) {
                PermTree permTree = new PermTree();
                permTree.setKey(String.valueOf(child.getId()));
                permTree.setTitle(child.getPermName());
                childList.add(permTree);
                // 递归调用
                addChildMenu(permTree, child, list);
            }
        }
    }

    /**
     * 根据 角色ID 获取角色权限ID列表
     * @param roleId
     * @return
     */
    public List<Long> getRolePerm(Long roleId){
        List<SysRolePerm> rolePerms = rolePermRepository.getByRoleId(roleId);
        List<Long> result = new ArrayList<>();
        for (SysRolePerm rolePerm : rolePerms){
            result.add(rolePerm.getPermId());
        }
        return result;
    }
}
