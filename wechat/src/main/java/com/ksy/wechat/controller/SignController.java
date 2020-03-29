package com.ksy.wechat.controller;

import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksy.wechat.dto.CommonResultDto;
import com.ksy.wechat.service.RestAuthService;
import com.ksy.wechat.service.RestRoomService;
import com.ksy.wechat.service.RestUserService;
import com.ksy.wechat.utill.CookieUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import com.ksy.wechat.dto.ChatUserDto;
import com.sun.org.apache.xml.internal.utils.URI;
import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SignController {
    @Autowired
    RestAuthService restAuthService;
    @Autowired
    RestUserService restUserService;

    @GetMapping("/sign")
    public String SignView(Model model) {
        // View attribute
        return "sign";
    }

    @PostMapping("/signup")
    @ResponseBody
    public String SignUp(@RequestBody ChatUserDto user) throws MalformedURIException {
        //TODO user에 대한 validate 체크
        ResponseEntity<String> response = restUserService.createUser(user);

        return response.getBody();
    }

    @PostMapping("/signin")
    @ResponseBody
    public String SignIn(@RequestBody ChatUserDto user, HttpServletResponse httpresponse, HttpServletRequest request) throws MalformedURIException, JsonProcessingException {
        final String uri = "http://localhost:8080/v1/auth/signin";
        ObjectMapper om = new ObjectMapper();
        ResponseEntity<String> response = restAuthService.readUserByEmail(user);
        CommonResultDto resultDto = om.readValue(response.getBody(), CommonResultDto.class);
        String token = resultDto.getData();
        //쿠키 저장.
        CookieUtill.create(httpresponse, "accesstoken", token, false, 3600, request.getServerName());

        return response.getBody();
    }
}
