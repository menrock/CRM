package com.niu.crm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.niu.crm.dao.config.DaoConfig;
import com.niu.crm.task.Scanned;

@Configuration
@Import(DaoConfig.class)
@ImportResource("classpath:META-INF/applicationContext-task.xml")
@ComponentScan(basePackageClasses = Scanned.class)
class TaskConfig extends WebMvcConfigurationSupport {
}
