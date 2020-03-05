package com.funwe.dao.repository;

import com.funwe.dao.entity.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * @author Administrator
 */
public interface SysRoleRepository extends JpaRepository<SysRole, Long>{
    /**
     * 根据用户名查找该用户所有权限
     * @param userName
     * @return List<SysRole>
     */
    @Query(value = "select r.* from sys_role r, sys_user_role ur where ur.user_name = ?1 and ur.role_id = r.id", nativeQuery = true)
    List<SysRole> findRolesOfUser(String userName);

    /**
     * 根据权限Id查找授权的角色
     * @param permId
     * @return List<SysRole>
     */
    @Query(value = "select r.* from sys_role r, sys_role_perm rp where rp.perm_id = ?1 and rp.role_id = r.id", nativeQuery = true)
    List<SysRole> findRolesOfPerm(long permId);

    /**
     * 获取分页角色
     * @param spec
     * @param pageable
     * @return
     */
    Page<SysRole> findAll(Specification<SysRole> spec, Pageable pageable);
}
