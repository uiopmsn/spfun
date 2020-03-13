package com.funwe.dao.repository;

import com.funwe.dao.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Administrator
 */
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {
    /**
     * 根据用户名查询返回用户角色
     * @param userName
     * @return SysUserRole
     */
    List<SysUserRole> findRolesByUserName(String userName);

    /**
     * 根据用户名删除用户所属角色
     * @param userName
     */
    void deleteByUserName(String userName);
}
