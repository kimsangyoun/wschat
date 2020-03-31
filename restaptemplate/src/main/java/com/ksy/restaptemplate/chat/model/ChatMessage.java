package com.ksy.restaptemplate.chat.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

import com.ksy.restaptemplate.chat.repo.ChatRoomRepository;
import com.ksy.restaptemplate.chat.service.ChatService;
import com.ksy.restaptemplate.config.security.JwtTokenProvider;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ChatMessage {
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String roomNm; // 방이름
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
    private long userCount; // 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용
    private Set<String> userList;
    @Builder
    public ChatMessage(MessageType type, String roomId,String roomNm, String sender, String message, long userCount,Set<String> userList) {
        this.type = type;
        this.roomId = roomId;
        this.roomNm = roomNm;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
        this.userList = userList;
    }
 
    // 메시지 타입 : 입장, 퇴장, 채팅
    public enum MessageType {
        ENTER, QUIT, TALK
    }
     

}