package com.example.LapTrinhMang3;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;

@Component
public class HttpLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Wrap request for caching
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }

        // Log request details
        String clientIp = getClientIp(request);
        System.out.println("Client IP: " + clientIp);
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request URL: " + request.getRequestURL());

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("Response Status: " + response.getStatus() +"- OutputStream: "+ response.getHeaderNames());

    }
    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For"); // Check if request goes through a proxy
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr(); // Fallback to remote address
        }
        return clientIp;
    }
}
