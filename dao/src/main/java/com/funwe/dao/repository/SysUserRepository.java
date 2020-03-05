package com.funwe.dao.repository;

import com.funwe.dao.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Administrator
 */
public interface SysUserRepository extends JpaRepository<SysUser, String> {
    /**
     * 获取分页用户
     * @param spec
     * @param pageable
     * @return
     */
    Page<SysUser> findAll(Specification<SysUser> spec, Pageable pageable);
}
