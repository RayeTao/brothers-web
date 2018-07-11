package com.taoran.brothers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
public class SpringBootStartApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStartApplication.class, args);
	}

	/**
	 * 文件上传临时路径
	 */
	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setLocation("E:/git/brothers/MediaRoot/images/");
		return factory.createMultipartConfig();
	}
}
