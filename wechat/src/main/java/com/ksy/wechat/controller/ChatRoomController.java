package com.ksy.wechat.controller;

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
public class ChatRoomController {
	@Autowired
	RestTemplate restTemplate;
	
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }


    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId,HttpServletRequest request) throws JsonMappingException, JsonProcessingException {
    	
    	final String uri = "http://localhost:8080/v1/auth/me";
    	System.out.println(roomId+"�� ��");
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
        	ObjectMapper om = new ObjectMapper();
        	SingleResultDto resultData = om.readValue(response.getBody(), SingleResultDto.class);
        	Map<String, Object> map = om.convertValue(resultData.getData(), Map.class);
        	String unm = (String)map.get("name");
        	model.addAttribute("userId", unm);
        	//resultData

        } else {
            System.out.println("Request Failed");
            System.out.println(response.getStatusCode());
        }
        model.addAttribute("accesstoken", token);
        model.addAttribute("roomId", roomId);
        return "/roomdetail";
    }
    @PostMapping("/room/makeroom")
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

    @GetMapping("/room/readroom")
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
