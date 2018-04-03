package com.plumdo.flow.config;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConfigurationProperties(prefix = "swagger")
public class Swagger2Configuration {
	@Autowired
	private TypeResolver typeResolver;

	private Map<String, String> properties = new HashMap<>();
	private Map<String, String> contact = new HashMap<>();
	private Map<String, String> error = new HashMap<>();
	
    public Map<String, String> getProperties() {
		return properties;
	}
    public Map<String, String> getContact() {
 		return contact;
 	}
    public Map<String, String> getError() {
 		return error;
 	}
    
	@Bean
    public Docket createRestApi() {
    	
        return new Docket(DocumentationType.SWAGGER_2)
        		.useDefaultResponseMessages(false)
        		.globalResponseMessage(RequestMethod.GET, getDefaultResponseMessage())
        		.globalResponseMessage(RequestMethod.POST, getDefaultResponseMessage())
        		.globalResponseMessage(RequestMethod.PUT, getDefaultResponseMessage())
        		.globalResponseMessage(RequestMethod.DELETE, getDefaultResponseMessage())
                .directModelSubstitute(Timestamp.class, Date.class)
                .tags(new Tag("默认标签", "定义全局默认标签"),getTags())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.plumdo.flow"))
                .paths(PathSelectors.any())
                .build();
    }
	
	private Tag[] getTags() {
		Tag[] tags = new Tag[3];
		tags[0] = new Tag("表单内容", "处理表单内容相关接口");
		tags[1] = new Tag("表单模型", "处理表单模型相关接口");
		tags[2] = new Tag("表单定义", "处理表单定义相关接口");
		return tags;
	}
	
	
	private List<ResponseMessage> getDefaultResponseMessage(){
		List<ResponseMessage> responseMessages =new ArrayList<ResponseMessage>();
    	responseMessages.add(new ResponseMessageBuilder().code(500).message(error.get("500")).responseModel(new ModelRef("ErrorInfo")).build());
    	responseMessages.add(new ResponseMessageBuilder().code(404).message(error.get("404")).responseModel(new ModelRef("ErrorInfo")).build());
    	return responseMessages;
	}
	
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(properties.get("title"))
        		.description(properties.get("description"))
        		.license(properties.get("license"))
        		.licenseUrl(properties.get("licenseUrl"))
                .contact(new Contact(contact.get("name"), contact.get("url"), contact.get("email")))
                .version(properties.get("version"))
                .build();
    }
}