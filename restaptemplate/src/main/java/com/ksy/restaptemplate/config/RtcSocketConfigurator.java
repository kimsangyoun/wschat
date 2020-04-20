package com.ksy.restaptemplate.config;

import lombok.extern.slf4j.Slf4j;
import org.nextrtc.signalingserver.NextRTCConfig;
import org.nextrtc.signalingserver.api.NextRTCServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.ksy.restaptemplate.chat.rtc.MyEndpoint;

import javax.servlet.http.HttpSession;
import java.util.Map;


@Slf4j
@Configuration
@Import(NextRTCConfig.class)
@EnableWebSocket
public class RtcSocketConfigurator implements WebSocketConfigurer {
    @Autowired
    private NextRTCServer server;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	System.out.println("등록은 된거니?");
        registry.addHandler(customEndpoint(), "/signaling")
                .setAllowedOrigins("*")
                .addInterceptors(new AssignSession());
    }

    @Bean
    public MyEndpoint customEndpoint() {
        return new MyEndpoint(server);
    }
    
    class AssignSession implements HandshakeInterceptor {
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
            if (request instanceof ServletServerHttpRequest) {
                log.info("Incoming request!!!");
                ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                HttpSession session = servletRequest.getServletRequest().getSession(false);
                if (session != null) {
                    attributes.put("HTTP_SESSION", session);
                }
            }
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

        }
    }

}