package com.ksy.wechat.service;

import com.ksy.wechat.dto.ChatRoomDto;
import com.ksy.wechat.utill.CookieUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class RestRoomService {
    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> createRoom(String token,ChatRoomDto room){
        //todo �봽濡쒗띁�떚濡� �룄硫붿씤 二쇱냼 �벑�벑 鍮쇱옄.
        final String uri = "https://localhost:8443/v1/room";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // build the request
        HttpEntity<ChatRoomDto> entity = new HttpEntity<>(room, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        return response;
    }
    public ResponseEntity<String> readRoomList(String token){
        final String uri = "https://localhost:8443/v1/rooms";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
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

        return response;
    }


}
