package com.file;

import org.h2.server.web.WebServlet;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.file.config.StorageProperties;
import com.file.service.FileService;

@SpringBootApplication
/**
 * Same as
 * 
 * @Configuration
 * @EnableAutoConfiguration
 * @ComponentScan
 */
@EnableConfigurationProperties(StorageProperties.class)
@EnableScheduling
public class FileApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileApiApplication.class, args);
	}
	
	@Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

	@Bean
	CommandLineRunner init(FileService fileService) {
		return (args) -> {
			fileService.deleteAll();
			fileService.init();
		};
	}
}
