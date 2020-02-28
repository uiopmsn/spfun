package com.funwe.core.ds;

import com.funwe.core.anno.UsedDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Aspect
@Component
public class DynamicDataSourceAspect {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, UsedDataSource ds) throws Throwable {
        String dsId = ds.value();
        if (DynamicDataSourceContextHolder.dataSourceIds.contains(dsId)) {
            DynamicDataSourceContextHolder.removeDataSourceRouterKey();
            DynamicDataSourceContextHolder.setDataSourceRouterKey(dsId);
        } else {
            logger.info("数据源[{}]不存在，使用默认数据源 >{}", dsId, point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceRouterKey("master");
        }

    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, UsedDataSource ds) {
        DynamicDataSourceContextHolder.removeDataSourceRouterKey();
    }

}
