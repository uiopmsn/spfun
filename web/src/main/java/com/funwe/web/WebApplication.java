package com.funwe.web;

import com.funwe.core.ds.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Administrator
 */
@Import(DynamicDataSourceRegister.class)
@EntityScan(basePackages = "com.funwe.dao.entity")
@EnableJpaRepositories(basePackages = "com.funwe.dao.repository")
@SpringBootApplication(scanBasePackages = "com.funwe")
public class WebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        //指定Spring配置
        return builder.sources(WebApplication.class);
    }

}
