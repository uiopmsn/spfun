package com.funwe.dao.repository.system;

import com.funwe.dao.entity.SysUserCurrentDatasource;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Administrator
 */
public interface SysUserDataSourceRepository extends JpaRepository<SysUserCurrentDatasource, Long> {
}
