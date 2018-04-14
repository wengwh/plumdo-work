package com.plumdo.common.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.plumdo.common.utils.DateUtils;
import com.plumdo.common.utils.ObjectUtils;

/**
 * 打印请求过滤器
 * 
 * @author wengwenhui
 *
 */
@Component
@Order(1)
public class RequestLogFilter extends OncePerRequestFilter {
	private Logger logger = LoggerFactory.getLogger(RequestLogFilter.class);

	private static String CHARSET_NAME_UTF_8 = "utf-8";
	private static int STR_MAX_LEN = 5000;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		long begintime = DateUtils.currentTimeMillis();
		long timecost = 0;
		String requestURI = request.getRequestURI();
		String method = request.getMethod();
		String ip = request.getRemoteAddr();
		if (isNotJsonContentType(request.getContentType())) {
			filterChain.doFilter(request, response);
			timecost = DateUtils.getTimeMillisConsume(begintime);
			logger.debug("ip:{} 调用接口,请求地址为:{}, 方式:{}, 请求参数为:{},[{}]ms", ip, requestURI, method, request.getQueryString(), timecost);
		}else {
			JsonContentCachingRequestWrapper requestWrapper = new JsonContentCachingRequestWrapper(request);
			ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
			filterChain.doFilter(requestWrapper, responseWrapper);
			timecost = DateUtils.getTimeMillisConsume(begintime);
			String requestParam = convertString(requestWrapper.getContentAsByteArray());
			updateResponse(requestURI, responseWrapper);
			if (isNotJsonContentType(responseWrapper.getContentType())) {
				logger.debug("ip:{} 调用接口,请求地址为:{}, 方式:{}, 请求参数为:{},[{}]ms", ip, requestURI, method, requestParam, timecost);
			} else {
				String result = convertString(responseWrapper.getContentAsByteArray());
				logger.debug("ip:{} 调用接口,请求地址为:{}, 方式:{}, 请求参数为:{},返回值是{},[{}]ms", ip, requestURI, method, requestParam, result, timecost);
			}
		}
		
	}

	private boolean isNotJsonContentType(String contentType) {
		if (contentType == null || !contentType.toLowerCase().startsWith("application/json")) {
			return true;
		} else {
			return false;
		}
	}

	private String convertString(byte[] contentByte) throws UnsupportedEncodingException {
		String contentStr = new String(contentByte, CHARSET_NAME_UTF_8);
		String postpfix = "......";
		if (ObjectUtils.isNotEmpty(contentStr)) {
			if (contentStr.length() <= STR_MAX_LEN) {
				return contentStr;
			} else {
				return contentStr.subSequence(0, STR_MAX_LEN) + postpfix;
			}
		} else {
			return "";
		}
	}

	private void updateResponse(String requestURI, ContentCachingResponseWrapper responseWrapper) throws IOException {
		try {
			HttpServletResponse rawResponse = (HttpServletResponse) responseWrapper.getResponse();
			byte[] body = responseWrapper.getContentAsByteArray();
			ServletOutputStream outputStream = rawResponse.getOutputStream();
			if (rawResponse.isCommitted()) {
				if (body.length > 0) {
					StreamUtils.copy(body, outputStream);
				}
			} else {
				if (body.length > 0) {
					rawResponse.setContentLength(body.length);
					StreamUtils.copy(body, rawResponse.getOutputStream());
				}
			}
			finishResponse(outputStream, body);
		} catch (Exception ex) {
			logger.error("请求地址为" + requestURI + "的连接返回报文失败,原因是{}", ex.getMessage());
		}
	}

	private void finishResponse(ServletOutputStream outputStream, byte[] body) throws IOException {
		if (body.length > 0) {
			outputStream.flush();
			outputStream.close();
		}
	}

}
