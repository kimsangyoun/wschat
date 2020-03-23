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
    @GetMapping("/roomdetail")
    public String MainViewDettail(Model model) {
        // View attribute
        return "roomdetail";
    }
    @PostMapping("/makeroom")
    @ResponseBody
    public String MakeRoom(@RequestBody ChatRoomDto room, HttpServletRequest request) throws MalformedURIException {

        final String uri = "http://localhost:8080/v1/room";

        String token = CookieUtill.getValue(request, "accesstoken");
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", token);
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // build the request
        HttpEntity<ChatRoomDto> entity = new HttpEntity<>(room, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
        return response.getBody().toString();
    }

    @GetMapping("/readroom")
    @ResponseBody
    public String ReadRoom(HttpServletRequest request) throws MalformedURIException {

        final String uri = "http://localhost:8080/v1/rooms";

        String token = CookieUtill.getValue(request, "accesstoken");
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", token);
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // build the request
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                String.class,
                1
        );
        // check response
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Request Successful.");
            System.out.println(response.getBody());

        } else {
            System.out.println("Request Failed");
            System.out.println(response.getStatusCode());
        }
        return response.getBody();
    }
}
