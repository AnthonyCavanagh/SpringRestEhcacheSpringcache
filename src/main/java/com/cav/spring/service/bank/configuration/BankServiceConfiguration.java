package com.cav.spring.service.bank.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ImportResource("classpath:/spring.xml")
@ComponentScan(basePackages = "com.cav.spring.service.bank")
public class BankServiceConfiguration {
	

}