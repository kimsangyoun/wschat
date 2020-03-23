package com.ksy.wechat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksy.wechat.dto.ChatRoomDto;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoomDto> chatRooms;

    public ChatService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoomDto> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoomDto findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoomDto createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
      //  ChatRoomDto chatRoom = new ChatRoomDto(randomId,name);

      //  chatRooms.put(randomId, chatRoom);
        return null;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}