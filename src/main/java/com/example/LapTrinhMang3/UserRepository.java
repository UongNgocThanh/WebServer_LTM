package com.example.LapTrinhMang3;

import com.example.LapTrinhMang3.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {
}
