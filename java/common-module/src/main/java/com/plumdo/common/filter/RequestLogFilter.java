package com.plumdo.common.filter;

import com.plumdo.common.utils.DateUtils;
import com.plumdo.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 打印请求过滤器
 *
 * @author wengwh
 * @date 2018/12/6
 */
@Slf4j
@Component
@Order(1)
public class RequestLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long beginTime = DateUtils.currentTimeMillis();
        long timeCost;
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        String ip = request.getRemoteAddr();
        if (isNotJsonContentType(request.getContentType())) {
            filterChain.doFilter(request, response);
            timeCost = DateUtils.getTimeMillisConsume(beginTime);
            log.debug("ip:{} 调用接口,请求地址为:{}, 方式:{}, 请求参数为:{},[{}]ms", ip, requestUri, method, request.getQueryString(), timeCost);
        } else {
            JsonContentCachingRequestWrapper requestWrapper = new JsonContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(requestWrapper, responseWrapper);
            timeCost = DateUtils.getTimeMillisConsume(beginTime);
            String requestParam = convertString(requestWrapper.getContentAsByteArray());
            updateResponse(requestUri, responseWrapper);
            if (isNotJsonContentType(responseWrapper.getContentType())) {
                log.debug("ip:{} 调用接口,请求地址为:{}, 方式:{}, 请求参数为:{},[{}]ms", ip, requestUri, method, requestParam, timeCost);
            } else {
                String result = convertString(responseWrapper.getContentAsByteArray());
                log.debug("ip:{} 调用接口,请求地址为:{}, 方式:{}, 请求参数为:{},返回值是{},[{}]ms", ip, requestUri, method, requestParam, result, timeCost);
            }
        }

    }

    private boolean isNotJsonContentType(String contentType) {
        return contentType == null || !contentType.toLowerCase().startsWith("application/json");
    }

    private String convertString(byte[] contentByte) throws UnsupportedEncodingException {
        String contentStr = new String(contentByte, "utf-8");
        String postFix = "......";
        if (ObjectUtils.isNotEmpty(contentStr)) {
            int strMaxLength = 5000;
            if (contentStr.length() <= strMaxLength) {
                return contentStr;
            } else {
                return contentStr.subSequence(0, strMaxLength) + postFix;
            }
        } else {
            return "";
        }
    }

    private void updateResponse(String requestUri, ContentCachingResponseWrapper responseWrapper) {
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
            log.error("请求地址为" + requestUri + "的连接返回报文失败,原因是{}", ex.getMessage());
        }
    }

    private void finishResponse(ServletOutputStream outputStream, byte[] body) throws IOException {
        if (body.length > 0) {
            outputStream.flush();
            outputStream.close();
        }
    }

}
