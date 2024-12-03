package com.example.LapTrinhMang3.Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter nếu cần
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Kiểm tra nếu request và response chưa phải là kiểu cần thiết thì bọc chúng
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper((HttpServletRequest) request);
        }

        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper((HttpServletResponse) response);
        }

        // Tiến hành chain để tiếp tục xử lý request
        chain.doFilter(request, response);

        // Sau khi hoàn thành request, lấy dữ liệu từ request và response
        logRequest((HttpServletRequest) request);
        logResponse((HttpServletResponse) response);
    }

    @Override
    public void destroy() {
        // Dọn dẹp khi filter bị hủy
    }

    // Log thông tin request
    private void logRequest(HttpServletRequest request) {
        String clientIp = getClientIp(request);
        System.out.println("Client IP: " + clientIp);
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request URL: " + request.getRequestURL());
    }

    // Log thông tin response
    private void logResponse(HttpServletResponse response) throws UnsupportedEncodingException {
        // Log status code
        System.out.println("Response Status: " + response.getStatus());

        // Log response body (dữ liệu response đã được cache trong ContentCachingResponseWrapper)
        ContentCachingResponseWrapper wrappedResponse = (ContentCachingResponseWrapper) response;
        String responseBody = new String(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding());
//        System.out.println("Response Body: " + responseBody);
    }

    // Lấy IP của client từ request
    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }
}
