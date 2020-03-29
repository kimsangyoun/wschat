package com.ksy.wechat.controller;

import com.ksy.wechat.service.RestAuthService;
import com.ksy.wechat.service.RestRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksy.wechat.dto.ChatRoomDto;
import com.ksy.wechat.dto.SingleResultDto;
import com.ksy.wechat.utill.CookieUtill;
import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/room")
public class ChatRoomController {
    @Autowired
    RestRoomService restRoomService;
    @Autowired
    RestAuthService restAuthService;

    @GetMapping("/")
    public String rooms(Model model) {
        return "/chat/room";
    }

    @GetMapping("/users")
    public String users(Model model) {
        return "/userlist";
    }


    @GetMapping("/enter/{roomId}")
    public String EnterRoom(Model model, @PathVariable String roomId,HttpServletRequest request){
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

        model.addAttribute("accesstoken", token);
        model.addAttribute("roomId", roomId);

        return "/roomdetail";
    }

    @PostMapping("/makeroom")
    @ResponseBody
    public String MakeRoom(@RequestBody ChatRoomDto room, HttpServletRequest request) {
        //todo room validate

        String token = CookieUtill.getValue(request, "accesstoken");
        ResponseEntity<String> response = restRoomService.createRoom(token,room);

        return response.getBody();
    }

    @GetMapping("/readroom")
    @ResponseBody
    public String ReadRoom(HttpServletRequest request) {
        String token = CookieUtill.getValue(request, "accesstoken");

        ResponseEntity<String> response = restRoomService.readRoomList(token);

        return response.getBody();
    }

}
