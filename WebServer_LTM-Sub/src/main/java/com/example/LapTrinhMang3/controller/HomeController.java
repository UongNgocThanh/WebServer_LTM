package com.example.LapTrinhMang3.controller;

import com.example.LapTrinhMang3.model.Account;
import com.example.LapTrinhMang3.service.AccountService;
import com.example.LapTrinhMang3.service.UserService;
import com.example.LapTrinhMang3.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String getUsers(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);  // Hiển thị 10 người dùng mỗi trang
        Page<Users> usersPage = userService.getAllUsers2(pageable);
        model.addAttribute("usersPage", usersPage);  // Truyền đối tượng Page vào model
        // Lấy thông tin người dùng hiện tại từ SecurityContext
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername(); // Lấy email từ UserDetails
            model.addAttribute("email", email);

            // Giả sử bạn cần thêm username, lấy từ database qua AccountService
            Account account = accountService.getAccountByEmail(email);
            model.addAttribute("username", account.getUsername());
        } else {
            model.addAttribute("email", "Guest");
            model.addAttribute("username", "Guest");
        }
        return "index";
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


}
