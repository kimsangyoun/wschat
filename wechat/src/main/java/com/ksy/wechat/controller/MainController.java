package com.ksy.wechat.controller;

import java.util.Collections;

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

import com.ksy.wechat.dto.ChatRoomDto;
import com.ksy.wechat.dto.ChatUserDto;
import com.sun.org.apache.xml.internal.utils.URI;
import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;
@Controller
public class MainController {
	@Autowired
	RestTemplate restTemplate;
	
    @GetMapping("/main")
    public String MainView(Model model) {
        // View attribute
        return "main";
    }
    @PostMapping("/makeroom")
    @ResponseBody
    public String MakeRoom(@RequestBody ChatRoomDto room) throws MalformedURIException {
    	
    	final String uri = "http://localhost:8080/v1/room";
    	//final String baseUrl = "http://localhost:"+randomServerPort+"/employees/";
        URI url = new URI(uri);
    
    	HttpHeaders headers = new HttpHeaders();
    	// set `content-type` header
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	// set `accept` header
    	headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    	// build the request
    	HttpEntity<ChatRoomDto> entity = new HttpEntity<>(room, headers);
	    ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
	    return response.getBody().toString();
    }
}
