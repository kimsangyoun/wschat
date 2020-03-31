package com.ksy.restaptemplate.chat.interceptor;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.ksy.restaptemplate.chat.model.ChatMessage;
import com.ksy.restaptemplate.chat.repo.ChatRoomRepository;
import com.ksy.restaptemplate.chat.service.ChatService;
import com.ksy.restaptemplate.config.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompInterceptor implements ChannelInterceptor {
 
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        //웹소켓 연결 요청일 떄에는 토큰 검증.
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwtToken = accessor.getFirstNativeHeader("X-AUTH-TOKEN");
            jwtTokenProvider.validateToken(jwtToken);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청 일때에는 
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            System.out.println("SUBSCRIBE CHAPTER01");
            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            System.out.println("SUBSCRIBE CHAPTER02 :: " +roomId);
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            System.out.println("SUBSCRIBE CHAPTER03 :: " +sessionId);
            List<String> headerlist = accessor.getNativeHeader("user");
            String name = headerlist.get(headerlist.size()-1);
            System.out.println("SUBSCRIBE CHAPTER03 :: " +name);
            // 채팅방에 들어온 클라이언트 srssionId와 userId를 맵핑해 놓는다.(해당 세션이 누구인지 알기 위해.)
            chatRoomRepository.setUserSessionInfo(sessionId,name);
            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            chatRoomRepository.setUserEnterInfo(sessionId, roomId);
            // 채팅방의 인원수를 +1한다.
            chatRoomRepository.plusUserCount(roomId);
            //채팅방에 들어온 유저의 정보들을 저장한다. (room 안에 유저 파악)
            chatRoomRepository.setRoomUserInfo(roomId,name);
            
            
            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).userList(chatRoomRepository.getUserListByRoomid(roomId)).build());
            log.info("SUBSCRIBED {}, {}", name, roomId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료

            String sessionId = (String) message.getHeaders().get("simpSessionId");
            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
            // 연결이 종료된 클라이언트 sesssionId로 유저 id를 얻는다.
            String name =  chatRoomRepository.getUserSessionUserId(sessionId);
            // 채팅방의 인원수를 -1한다.
            chatRoomRepository.minusUserCount(roomId);
            // 채팅방의 유저를 제거한다.
            chatRoomRepository.removeUserEnterRoom(roomId,name);
            // 유저 세션 정보를 제거 한다.
            chatRoomRepository.removeUserInfo(sessionId);
            // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(name).userList(chatRoomRepository.getUserListByRoomid(roomId)).build());
            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
            chatRoomRepository.removeUserEnterInfo(sessionId);
            
            if(chatRoomRepository.getUserCount(roomId) <1) {
            	chatRoomRepository.deleteChatRoom(roomId);
            }
            
            log.info("DISCONNECTED {}, {}", sessionId, roomId);
        }
        return message;
    }

}
