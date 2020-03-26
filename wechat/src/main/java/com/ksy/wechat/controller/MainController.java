package com.ksy.wechat.controller;

import java.util.Collections;

import com.ksy.wechat.utill.CookieUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.ksy.wechat.dto.ChatRoomDto;
import com.ksy.wechat.dto.ChatUserDto;
import com.sun.org.apache.xml.internal.utils.URI;
import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/main")
    public String MainView(Model model) {
        // View attribute
        return "main";
    }
   

}
