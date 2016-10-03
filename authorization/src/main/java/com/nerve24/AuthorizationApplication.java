package com.nerve24;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.util.InMemoryResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableAsync
@EnableAutoConfiguration
@ComponentScan("com.nerve24.entity")
@EnableResourceServer
public class AuthorizationApplication extends SpringBootServletInitializer{
	
	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}
	
	@Profile("!cloud")
    @Bean
    RequestDumperFilter requestDumperFilter() {
        return new RequestDumperFilter();
    }
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@PostConstruct
    public void runScripts() {
    	DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    	dataSourceInitializer.setDataSource(dataSource);    	
    	System.out.println("Running Script start.........");
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		Resource resource = new InMemoryResource("classpath:oauth2.sql");
		
		populator.addScript(resource);
		dataSourceInitializer.setDatabasePopulator(populator);
		//DatabasePopulatorUtils.execute(populator, dataSource);
		System.out.println("Running Script end.........");
	}
	
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
}
