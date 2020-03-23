package com.ksy.wechat.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksy.wechat.dto.ChatUserDto;
import com.ksy.wechat.dto.CommonResultDto;
import com.sun.org.apache.xml.internal.utils.URI;
import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;

@Controller
public class SignController {
	@Autowired
	RestTemplate restTemplate;
	
    @GetMapping("/sign")
    public String SignView(Model model) {
        // View attribute
        return "sign";
    }
    
    @PostMapping("/signup")
    @ResponseBody
    public String SignUp(@RequestBody ChatUserDto user) throws MalformedURIException {
    	 

    	final String uri = "http://localhost:8080/v1/user";
    	//final String baseUrl = "http://localhost:"+randomServerPort+"/employees/";
        URI url = new URI(uri);
    
    	HttpHeaders headers = new HttpHeaders();
    	// set `content-type` header
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	// set `accept` header
    	headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    	// build the request
    	HttpEntity<ChatUserDto> entity = new HttpEntity<>(user, headers);
	    ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
	    return response.getBody().toString();
    }
    @PostMapping("/signin")
    @ResponseBody
    public String SignIn(@RequestBody ChatUserDto user) throws MalformedURIException {
    	final String uri = "http://localhost:8080/v1/auth/signin";
        URI url = new URI(uri);
    
    	HttpHeaders headers = new HttpHeaders();
    	// set `content-type` header
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	// set `accept` header
    	headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    	// build the request
    	HttpEntity<ChatUserDto> entity = new HttpEntity<>(user, headers);
	    ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
	    return response.getBody().toString();
    }
}
