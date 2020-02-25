package com.funwe.dao.repository.system;

import com.funwe.dao.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Administrator
 */
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {
    /**
     * 根据用户名查询返回第一个用户角色
     * @param userName
     * @return SysUserRole
     */
    SysUserRole findFirstByUserName(String userName);
}
