package com.example.LapTrinhMang3;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class OnlineUserService {

    private Set<String> onlineUsers = new HashSet<>();  // Set để lưu tài khoản người dùng online

    // Thêm người dùng vào danh sách online
    public void addOnlineUser(String username) {
        onlineUsers.add(username);
    }

    // Xóa người dùng khỏi danh sách online
    public void removeOnlineUser(String username) {
        onlineUsers.remove(username);
    }

    // Lấy số lượng người dùng online
    public int getOnlineUserCount() {
        return onlineUsers.size();
    }
}
