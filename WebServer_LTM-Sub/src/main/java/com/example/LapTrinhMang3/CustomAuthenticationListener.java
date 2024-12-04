package com.example.LapTrinhMang3;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationListener {

    @Autowired
    private OnlineUserService onlineUserService;

    // Xử lý sự kiện đăng nhập thành công
    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        String username = authentication.getName();  // Lấy tên người dùng từ Authentication
        onlineUserService.addOnlineUser(username);  // Thêm người dùng vào danh sách online
    }

    // Xử lý sự kiện đăng xuất
    @EventListener
    public void handleLogout(HttpServletRequest request, HttpServletResponse response) {
        // Lấy tên người dùng từ session hoặc authentication
        String username = (String) request.getSession().getAttribute("username");
        if (username != null) {
            onlineUserService.removeOnlineUser(username);  // Xóa người dùng khỏi danh sách online
        }
    }
}
