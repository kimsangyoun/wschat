package com.ksy.wechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignController {
    @GetMapping("/sign")
    public String SignInView(Model model) {
        // View attribute
        System.out.println("???");
        model.addAttribute("template", "contents/signin");
        return "signin";
    }
}
