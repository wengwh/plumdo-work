package com.plumdo.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 流程引擎服务类
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
@SpringBootApplication(scanBasePackages = "com.plumdo")
public class FlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowApplication.class, args);
    }

}