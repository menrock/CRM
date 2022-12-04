package com.niu.crm.web.config;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;


@Configuration
@ImportResource({ "classpath:META-INF/applicationContext-web.xml" })
@ComponentScan(basePackages = "com.niu.crm.web", includeFilters = @Filter({Controller.class,ControllerAdvice.class}) , useDefaultFilters = false)
//@ComponentScan(basePackageClasses = Scanned.class, includeFilters = @Filter({Controller.class,ControllerAdvice.class}), useDefaultFilters = false)
// @EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurationSupport {
	private static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);
	private static final String MESSAGE_SOURCE = "/WEB-INF/i18n/messages";
	
	private static final int maxPageSize = 200;

	/*
	@Override
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping requestMappingHandlerMapping = super.requestMappingHandlerMapping();
		requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
		requestMappingHandlerMapping.setUseTrailingSlashMatch(false);
		return requestMappingHandlerMapping;
	} */
	
	@Bean
	public MultipartResolver multipartResolver() {
		org.springframework.web.multipart.commons.CommonsMultipartResolver resolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
		
		//20M
		long maxUploadSize = 1024 *1024 *20L;
		
		resolver.setMaxUploadSize(maxUploadSize);
		return resolver;
	}
	
	@Bean
	public ViewResolver viewResolver() {
		logger.info("ViewResolver");
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	/*
	@Bean
	public TemplateResolver templateResolver()
	{
		org.thymeleaf.templateresolver.ServletContextTemplateResolver 
		templateResolver = new org.thymeleaf.templateresolver.ServletContextTemplateResolver();
		
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}
	
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver() );
		
		templateEngine.addDialect(new SpringSecurityDialect());
		//templateEngine.addDialect(new LayoutDialect());
		//templateEngine.addDialect(new VdlmDialect(resourceFacade));
		return templateEngine;
	} 

	@Bean
	public ThymeleafViewResolver viewResolver1() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		viewResolver.setOrder(1);
		return viewResolver;
	} */
	
	@Bean
	public HandlerMapping resourceHandlerMapping() {
		logger.info("resourceHandlerMapping");
		return super.resourceHandlerMapping();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		logger.info("addResourceHandlers");
		registry.addResourceHandler("/_resources/**").addResourceLocations("/_resources/");
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	    PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
	    resolver.setMaxPageSize(maxPageSize);
	    resolver.setSizeParameterName("rows");
	    resolver.setPageParameterName("page");
	    resolver.setOneIndexedParameters(true);
	    argumentResolvers.add(resolver);
	}

	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(MESSAGE_SOURCE);
		messageSource.setCacheSeconds(5);
		return messageSource;
	}
}
