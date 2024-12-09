package com.example.demo.SecutiyConfig;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;


@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

@Bean
public TemplateEngine textTemplateEngine() {
    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.addTemplateResolver(textTemplateResolver());
    return templateEngine;
}

@Bean
public StringTemplateResolver textTemplateResolver() {
    StringTemplateResolver templateResolver = new StringTemplateResolver();
    templateResolver.setTemplateMode("TEXT"); 
    templateResolver.setCacheable(false); 
    return templateResolver;
}

@Bean
public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587); 
    mailSender.setUsername("imsinfo05@gmail.com");
    mailSender.setPassword("cbtm wvyf tjcc kaaa");

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true"); 

    return mailSender;
}
}
