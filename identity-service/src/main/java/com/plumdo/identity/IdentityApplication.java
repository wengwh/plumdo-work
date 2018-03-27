package com.plumdo.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 人员服务启动类
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
@SpringBootApplication(scanBasePackages = "com.plumdo")
public class IdentityApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityApplication.class, args);
	}
}
