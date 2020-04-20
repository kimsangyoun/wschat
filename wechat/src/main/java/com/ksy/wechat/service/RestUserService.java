package com.ksy.wechat.service;

import com.ksy.wechat.dto.ChatRoomDto;
import com.ksy.wechat.dto.ChatUserDto;
import com.ksy.wechat.utill.CookieUtill;
import com.sun.org.apache.xml.internal.utils.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class RestUserService {
    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> createUser(ChatUserDto user){
        final String uri = "https://localhost:8443/v1/user";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<ChatUserDto> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        return response;
    }
    

}
