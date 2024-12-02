//package com.example.LapTrinhMang3;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class ResponseWrappingFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        if (response instanceof HttpServletResponse) {
//            CustomResponseWrapper wrappedResponse = new CustomResponseWrapper((HttpServletResponse) response);
//            chain.doFilter(request, wrappedResponse);
//        } else {
//            chain.doFilter(request, response);
//        }
//    }
//}
