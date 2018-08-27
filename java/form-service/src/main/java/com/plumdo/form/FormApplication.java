package com.plumdo.form;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 表单接口
 *
 * @author wengwh
 * @date 2018年8月27日
 */
@SpringBootApplication(scanBasePackages = "com.plumdo")
public class FormApplication {

	public static void main(String[] args) {
		SpringApplication.run(FormApplication.class, args);
	}
}
