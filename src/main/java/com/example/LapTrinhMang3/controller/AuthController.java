package com.example.LapTrinhMang3.controller;

import com.example.LapTrinhMang3.model.Account;
import com.example.LapTrinhMang3.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AccountService accountService;


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password!");
        }
        return "login"; // Tên file HTML cho trang đăng nhập
    }


    @GetMapping("/register")
    public String registerForm() {
        return "register"; // Tên file HTML cho trang đăng ký
    }

    @PostMapping("/register")
    public String register(Account account, Model model) {
        try {
            accountService.registerAccount(account);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed!");
            return "register";
        }
    }

    @GetMapping("/index")
    public String index() {
        return "/"; // Tên file HTML cho trang chính
    }
}
