package com.ksy.restaptemplate.chat.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.ksy.restaptemplate.chat.model.ChatMessage;
import com.ksy.restaptemplate.chat.repo.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {
 
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatRoomRepository chatRoomRepository;
 
    /**
     * destination정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }
 
    /**
     * 채팅방에 메시지 발송
     */
    public void sendChatMessage(ChatMessage chatMessage) {
        System.out.println("sendChatMessage 01");
        chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));
        System.out.println("sendChatMessage 02" + chatMessage.getUserCount());
        System.out.println("sendChatMessage 03" + chatMessage.getType());
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            System.out.println("sendChatMessage 04" + chatMessage.getSender());
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }
        try {
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
 
}