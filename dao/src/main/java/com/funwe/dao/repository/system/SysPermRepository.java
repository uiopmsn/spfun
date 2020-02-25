package com.funwe.dao.repository.system;

import com.funwe.dao.entity.SysPerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * @author Administrator
 */
public interface SysPermRepository extends JpaRepository<SysPerm, Long> {
    /**
     * 根据用户名获取菜单
     * @param userName
     * @return
     */
    @Query(value = "select distinct p.* " +
            " from sys_user u " +
            " join sys_user_role ur on u.user_name = ur.user_name " +
            " join sys_role_perm rp on ur.role_id = rp.role_id " +
            " join sys_perm p on rp.perm_id = p.id " +
            " where p.type = 'menu' " +
            " and u.user_name = ?1 " +
            " order by p.pid, p.order_by", nativeQuery = true)
    List<SysPerm> findPermOfUser(String userName);
}
