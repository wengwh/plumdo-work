package com.plumdo.common.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.plumdo.common.constant.CoreConstant;
import com.plumdo.common.utils.ObjectUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * JWT校验过滤器
 *
 * @author wengwenhui
 * @date 2018年4月2日
 */
@Component
@Order(3)
public class JwtValidFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (CoreConstant.JWT_AUTH_EXCLUDE_URL.equals(request.getRequestURI()) || request.getMethod().equals("OPTIONS")) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = request.getHeader("token");
		if (ObjectUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		Date expiration = null;
		try {
			token = token.substring(7);
			Claims claims = Jwts.parser().setSigningKey(CoreConstant.JWT_SECRET).parseClaimsJws(token).getBody();
			expiration = claims.getExpiration();
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		if (expiration != null && expiration.before(new Date())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		filterChain.doFilter(request, response);

	}

}
