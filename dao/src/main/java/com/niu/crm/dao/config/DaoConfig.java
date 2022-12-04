package com.niu.crm.dao.config;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;


@Configuration
@MapperScan(basePackages = { "com.niu.crm.dao.mapper"})
public class DaoConfig {
	private static Logger logger = LoggerFactory.getLogger(DaoConfig.class);

	/** 配置文件路径 */
	public static final String MAPPING_LOCATION = "classpath:/mapper/**/*.xml";

	public static final String PROFILE_NAME_DEV = "dev";
	public static final String PROFILE_NAME_TEST = "test";
	public static final String PROFILE_NAME_PROD = "prod";

	/* 当前启用的 */
	@Value("${profiles.active}")
	private String PROFILES_ACTIVE;

	@Profile(PROFILE_NAME_DEV)
	@Bean(name = "propertyPlaceholderConfigurer")
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurerDev() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		ClassPathResource resource = new ClassPathResource("env/config-dev.properties");
		ppc.setLocation(resource);
		logger.info("env/config-dev.properties loaded");
		return ppc;
	}

	@Profile(PROFILE_NAME_TEST)
	@Bean(name = "propertyPlaceholderConfigurer")
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurerTest() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		ClassPathResource resource = new ClassPathResource("env/config-test.properties");
		ppc.setLocation(resource);
		logger.info("env/config-test.properties loaded");
		return ppc;
	}

	@Profile(PROFILE_NAME_PROD)
	@Bean(name = "propertyPlaceholderConfigurer")
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurerProd() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		ClassPathResource resource = new ClassPathResource("env/config-prod.properties");
		ppc.setLocation(resource);
		logger.info("env/config-prod.properties loaded");
		return ppc;
	}

	@Value("${jdbc.url}")
	private String jdbc_url;
	@Value("${jdbc.username}")
	private String jdbc_user;
	@Value("${jdbc.password}")
	private String jdbc_password;

	/*
	@Bean(initMethod = "init", destroyMethod = "close")
	public DataSource dataSource() throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setName("crm");
		dataSource.setUrl(jdbc_url);
		dataSource.setUsername(jdbc_user);
		dataSource.setPassword(jdbc_password);

		// 加密，统计
		//dataSource.setFilters("config,stat");
		//dataSource.setConnectionProperties("config.decrypt=true");
		

		// <property name="connectionInitSqls" value="set names utf8mb4；" />
		Collection<String> list = new ArrayList<String>();
		list.add("set names utf8mb4;");
		dataSource.setConnectionInitSqls(list);

		dataSource.setInitialSize(2);
		dataSource.setMinIdle(2);
		dataSource.setMaxActive(100);
		// 连接等待超时时间
		dataSource.setMaxWait(60000);
		// 连接超时关闭时间
		dataSource.setTimeBetweenEvictionRunsMillis(60000);
		// 连接最小生存时间
		dataSource.setMinEvictableIdleTimeMillis(300000);
		dataSource.setValidationQuery("select 1");
		dataSource.setTestWhileIdle(true);
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		dataSource.setRemoveAbandoned(true);
		dataSource.setRemoveAbandonedTimeout(600);
		dataSource.setLogAbandoned(true);

		return dataSource;
	}
	*/

	@Bean(destroyMethod = "close")
	public DataSource dataSource() throws SQLException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		
		try{
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
			dataSource.setJdbcUrl(jdbc_url);
			dataSource.setUser(jdbc_user);
			dataSource.setPassword(jdbc_password);
			
			dataSource.setMinPoolSize(3);
			dataSource.setMaxPoolSize(30);
			dataSource.setIdleConnectionTestPeriod(18000);
			dataSource.setMaxIdleTime(25000);
			dataSource.setPreferredTestQuery("SELECT @@SQL_MODE" );
			
			dataSource.setTestConnectionOnCheckin(true);
			dataSource.setTestConnectionOnCheckout(false);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return dataSource;
	}

	@Bean(name = "transactionManager")
	public DataSourceTransactionManager transactionManager() throws SQLException {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource());
		return transactionManager;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		//sessionFactoryBean.setConfigLocation(configLocation);
		sessionFactoryBean.setDataSource(dataSource());
		Resource[] mapperLocations = getMapperLocations();
		sessionFactoryBean.setMapperLocations(mapperLocations);
		return sessionFactoryBean.getObject();
	}

	public Resource[] getMapperLocations() throws IOException {
		logger.info("资源文件加载...");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources(MAPPING_LOCATION);
		return resources;
	}
}
