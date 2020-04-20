package com.ksy.restaptemplate.chat.rtc;


import org.nextrtc.signalingserver.api.NextRTCServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class MyEndpoint extends TextWebSocketHandler {

    private final NextRTCServer server;

    @Autowired
	
    public MyEndpoint(NextRTCServer server) {
        this.server = server;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        server.register(new SessionWrapper(session));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        server.handle(NextRTCServer.MessageDecoder.decode(message.getPayload()), new SessionWrapper(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        server.unregister(new SessionWrapper(session), status.getReason());
    }
}
