package com.ksy.wechat.controller;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksy.wechat.dto.SingleResultDto;
import com.ksy.wechat.service.RestAuthService;
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
    RestAuthService restAuthService;

    @GetMapping("/main")
    public String MainView(Model model,HttpServletRequest request) {
        // 토큰 획득.
        String token = CookieUtill.getValue(request, "accesstoken");
        ResponseEntity<String> response = restAuthService.readUserByToken(token);
        // check response
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper om = new ObjectMapper();
                SingleResultDto resultData = om.readValue(response.getBody(), SingleResultDto.class);
                Map<String, Object> map = om.convertValue(resultData.getData(), Map.class);
                String unm = (String)map.get("name");
                model.addAttribute("userId", unm);
            }catch (JsonProcessingException e){
                //JsonProcessingException
            }

        } else {
            //todo 이름정보 못가져왔을 경우에는 어떻게 할것인가.
            System.out.println("Request Failed");
            System.out.println(response.getStatusCode());
        }
        return "main";
    }
   

}
