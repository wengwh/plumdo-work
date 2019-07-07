package com.plumdo.common.annotation;

import java.lang.annotation.*;


/**
 * 无须认证
 *
 * @author wengwenhui
 * @date 2018年4月12日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface NotAuth {

}
