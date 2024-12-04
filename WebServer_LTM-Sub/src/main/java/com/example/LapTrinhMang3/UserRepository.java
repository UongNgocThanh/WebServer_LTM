package com.example.LapTrinhMang3;

import com.example.LapTrinhMang3.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users,Long> {
    // Tìm kiếm nhân viên theo tên hoặc email
    Page<Users> findByUsernameContainingOrEmailContaining(String username, String email, Pageable pageable);
}
