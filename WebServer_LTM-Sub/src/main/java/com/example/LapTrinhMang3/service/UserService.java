package com.example.LapTrinhMang3.service;

import com.example.LapTrinhMang3.UserRepository;
import com.example.LapTrinhMang3.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Phương thức sửa lại để nhận Pageable và trả về Page<Users>
    public Page<Users> getAllUsers2(Pageable pageable) {
        return userRepository.findAll(pageable);  // Dùng findAll có Pageable từ JPA
    }
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    public Users updateUser(Long id, Users userDetails) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow();

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setDob(userDetails.getDob());
        existingUser.setGender(userDetails.getGender());
        existingUser.setAddress(userDetails.getAddress());
        existingUser.setPhoneNumber(userDetails.getPhoneNumber());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setJobTitle(userDetails.getJobTitle());
        existingUser.setStartDate(userDetails.getStartDate());
        existingUser.setSalary(userDetails.getSalary());
        existingUser.setBonus(userDetails.getBonus());
        existingUser.setWorkingHours(userDetails.getWorkingHours());

        return userRepository.save(existingUser);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Page<Users> getUsersPage(Pageable pageable) {
        return userRepository.findAll(pageable);  // Lấy dữ liệu phân trang từ UserRepository
    }
}

