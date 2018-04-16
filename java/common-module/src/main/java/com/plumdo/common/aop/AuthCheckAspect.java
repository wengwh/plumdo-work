package com.plumdo.common.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.plumdo.common.constant.CoreConstant;
import com.plumdo.common.exception.ExceptionFactory;
import com.plumdo.common.model.Authentication;
import com.plumdo.common.utils.ObjectUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


/**
 * jwt检验类，通过注解屏蔽部分接口
 *
 * @author wengwenhui
 * @date 2018年4月12日
 */
@Aspect
@Component
@Order(1)
public class AuthCheckAspect {
	private Logger logger = LoggerFactory.getLogger(AuthCheckAspect.class);
	@Autowired
	private ExceptionFactory exceptionFactory;
	
    @Pointcut("execution(public * com.plumdo..*.resource.*.*(..))")
    public void webRequestAuth() {}
    
    @Pointcut("!@within(com.plumdo.common.annotation.NotAuth) && !@annotation(com.plumdo.common.annotation.NotAuth)")
    public void webRequestNotAuth() {}

    @Around("webRequestAuth()&& webRequestNotAuth()")
    public Object doAuth(ProceedingJoinPoint pjp) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

		String userId = null;
		String token = request.getHeader("Token");
		if(ObjectUtils.isEmpty(token)) {
			token = request.getParameter("token");
		}
        logger.info("token:"+token);
        
        if(ObjectUtils.isEmpty(token)) {
        	exceptionFactory.throwAuthError(CoreConstant.HEADER_TOKEN_NOT_FOUND);
        }
        if(!token.startsWith("Bearer ")) {
        	exceptionFactory.throwAuthError(CoreConstant.HEADER_TOKEN_ILLEGAL);
        }
		
		try {
			token = token.substring(7);
			Claims claims = Jwts.parser().setSigningKey(CoreConstant.JWT_SECRET).parseClaimsJws(token).getBody();
			userId = claims.getId();
			if(ObjectUtils.isEmpty(userId)) {
				exceptionFactory.throwAuthError(CoreConstant.TOKEN_UERID_NOT_FOUND);
			}
			
			Date expiration = claims.getExpiration();
			if (expiration != null && expiration.before(new Date())) {
				exceptionFactory.throwAuthError(CoreConstant.TOKEN_EXPIRE_OUT);
			}
		} catch (Exception e) {
			exceptionFactory.throwAuthError(CoreConstant.TOKEN_AUTH_CHECK_ERROR);
		}
        try {
        	Authentication.setUserId(userId);
    		return pjp.proceed(pjp.getArgs());
        }finally {
        	Authentication.clear();
		}
    }
   
}