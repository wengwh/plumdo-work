package com.plumdo.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 请求跨域过滤器
 * 
 * @author wengwenhui
 *
 */
@Component
@Order(2)
public class RequestCrossFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// Access-Control-Allow-Origin: 指定授权访问的域
		response.addHeader("Access-Control-Allow-Origin", "*"); // 此优先级高于@CrossOrigin配置

		// Access-Control-Allow-Methods: 授权请求的方法（GET, POST, PUT, DELETE，OPTIONS等)
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");

		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Token, User-ID");

		response.addHeader("Access-Control-Max-Age", "1800");// 30 min

		filterChain.doFilter(request, response);
	}
}