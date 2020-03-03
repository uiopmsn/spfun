package com.funwe.dao.repository.system;

import com.funwe.dao.entity.SysRolePerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * @author Administrator
 */
public interface SysRolePermRepository extends JpaRepository<SysRolePerm, Long> {
    /**
     * 根据角色ID 删除角色权限
     * @param roleId
     * @return
     */
    void deleteByRoleId(long roleId);

    /**
     * 根据 角色ID 获取角色权限
     * @param roleId
     * @return
     */
    List<SysRolePerm> getByRoleId(Long roleId);
}
