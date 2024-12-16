package com.example.library;

import com.example.library.filter.ApiRateLimitFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<ApiRateLimitFilter> rateLimitingFilter() {
		FilterRegistrationBean<ApiRateLimitFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ApiRateLimitFilter());
		registrationBean.addUrlPatterns("/api/v1/books/*"); // Register filter for API endpoints
		return registrationBean;
	}


}
