package com.plumdo.form.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    super.addResourceHandlers(registry);
	    //change swagger-ui  mapping so swagger-ui can be served
	    if (!registry.hasMappingForPattern("/swagger-ui.html")) {
	        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/static/");
	    }
	}
}