package com.ksy.restaptemplate.chat.rtc;

import org.nextrtc.signalingserver.api.NextRTCServer;
import org.nextrtc.signalingserver.domain.Connection;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

public class SessionWrapper implements Connection {

    private final WebSocketSession session;

    public SessionWrapper(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public String getId() {
        return session.getId();
    }

    @Override
    public boolean isOpen() {
        return session.isOpen();
    }

    @Override
    public void sendObject(Object object) {
        try {
            session.sendMessage(new TextMessage(NextRTCServer.MessageEncoder.encode(object)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Principal getPrincipal() {
        return session.getPrincipal();
    }

    @Override
    public void close() throws IOException {
        session.close();
    }

    public void invalidate() {
        HttpSession httpSession = (HttpSession) session.getAttributes().get("HTTP_SESSION");
        httpSession.invalidate();
    }
}
