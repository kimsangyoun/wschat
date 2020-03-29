package com.ksy.wechat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksy.wechat.dto.ChatUserDto;
import com.ksy.wechat.dto.CommonResultDto;
import com.ksy.wechat.utill.CookieUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class RestAuthService {
    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> readUserByToken(String token){
        //todo 프로퍼티로 도메인 주소 등등 빼자.
        final String uri = "http://localhost:8080/v1/auth/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                String.class,
                1
        );

        return response;
    }
    public ResponseEntity<String> readUserByEmail(ChatUserDto user){
        //todo 프로퍼티로 도메인 주소 등등 빼자.
        final String uri = "http://localhost:8080/v1/auth/signin";
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // build the request
        HttpEntity<ChatUserDto> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        return response;
    }
}
