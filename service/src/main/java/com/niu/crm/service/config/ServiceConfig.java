package com.niu.crm.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.niu.crm.service.impl")
@EnableTransactionManagement
@ImportResource({ "classpath:/META-INF/applicationContext-service.xml"})
public class ServiceConfig {

}