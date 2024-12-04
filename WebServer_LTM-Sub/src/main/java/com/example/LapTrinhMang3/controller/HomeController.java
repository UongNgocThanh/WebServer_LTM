package com.example.LapTrinhMang3.controller;

import com.example.LapTrinhMang3.model.Account;
import com.example.LapTrinhMang3.service.AccountService;
import com.example.LapTrinhMang3.service.UserService;
import com.example.LapTrinhMang3.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/")
    public String getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String search, Model model) {
        Pageable pageable = PageRequest.of(page, 10);  // Hiển thị 10 người dùng mỗi trang

        // Tìm kiếm hoặc lấy tất cả người dùng
        Page<Users> usersPage;
        if (search != null && !search.isEmpty()) {
            // Tìm kiếm người dùng theo từ khóa
            usersPage = userService.searchUsers(search, pageable);
        } else {
            // Nếu không có tìm kiếm, lấy tất cả người dùng
            usersPage = userService.getAllUsers2(pageable);
        }

        // Truyền đối tượng Page vào model
        model.addAttribute("usersPage", usersPage);

        // Thêm thông tin người dùng hiện tại từ SecurityContext
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername(); // Lấy email từ UserDetails
            model.addAttribute("email", email);

            // Lấy thông tin tài khoản từ email
            Account account = accountService.getAccountByEmail(email);
            model.addAttribute("username", account.getUsername());
        } else {
            model.addAttribute("email", "Guest");
            model.addAttribute("username", "Guest");
        }

        // Thêm giá trị của search vào model để giữ lại sau khi submit form tìm kiếm
        model.addAttribute("search", search);

        return "index";  // Trả về trang index.html
    }




    @GetMapping("/list/add-user")
    public String addUserForm(Model model) {
        model.addAttribute("user", new Users()); // Tạo object user rỗng
        return "add-user"; // Trang HTML cho thêm người dùng
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute Users user, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user); // Gọi phương thức lưu dữ liệu
            redirectAttributes.addFlashAttribute("message", "Thêm nhân viên thành công!");
            return "redirect:/"; // Chuyển hướng về trang danh sách sau khi lưu thành công
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Thêm nhân viên thất bại!");
            return "redirect:/"; // Chuyển hướng về trang danh sách nếu có lỗi
        }
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute Users user) {
        user.setId(id);
        userService.updateUser(id, user); // Dùng updateUser thay vì createUser
        return "redirect:/"; // Quay lại danh sách
    }

    @GetMapping("/list/edit-user/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        Users user = userService.getUserById(id).orElse(null);
        model.addAttribute("user", user);
        return "edit-user"; // Trang sửa
    }
    @GetMapping("/list/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/"; // Quay lại danh sách sau khi xóa
    }




    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportUsersToCSV() {
        List<Users> usersList = userService.getAllUsers(); // Lấy toàn bộ người dùng

        // Tạo định dạng ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Tạo nội dung CSV
        StringBuilder csvContent = new StringBuilder("ID,Username,Email,Phone Number,DOB,Gender,Address,Job Title,Start Date,Salary,Bonus,Working Hours\n");

        for (Users user : usersList) {
            // Chuyển đổi DOB thành định dạng yyyy-MM-dd
            String dobFormatted = "";
            if (user.getDob() != null) {
                dobFormatted = dateFormat.format(user.getDob());
            }

            // Chuyển đổi StartDate thành định dạng yyyy-MM-dd
            String startDateFormatted = "";
            if (user.getStartDate() != null) {
                startDateFormatted = dateFormat.format(user.getStartDate());
            }

            csvContent.append(user.getId()).append(",")
                    .append(user.getUsername()).append(",")
                    .append(user.getEmail()).append(",")
                    .append(user.getPhoneNumber()).append(",")
                    .append(dobFormatted).append(",")
                    .append(user.getGender()).append(",")
                    .append(user.getAddress()).append(",")
                    .append(user.getJobTitle()).append(",")
                    .append(startDateFormatted).append(",")
                    .append(user.getSalary()).append(",")
                    .append(user.getBonus()).append(",")
                    .append(user.getWorkingHours()).append("\n");
        }

        // Chuyển đổi thành byte array
        byte[] csvBytes = csvContent.toString().getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=users.csv");

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }
}
