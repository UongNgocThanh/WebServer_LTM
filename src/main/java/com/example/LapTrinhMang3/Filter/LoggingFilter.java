package com.example.LapTrinhMang3.Filter;
import jakarta.servlet.*;

import java.io.IOException;

public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("Request: " + request);
        chain.doFilter(request, response);
        System.out.println("Response: " + response);
    }

    @Override
    public void destroy() {
        // Dọn dẹp filter
    }
}
