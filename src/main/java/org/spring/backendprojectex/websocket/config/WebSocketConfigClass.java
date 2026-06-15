package org.spring.backendprojectex.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigClass implements WebSocketMessageBrokerConfigurer {
    //메모리 기반 메시지 브로커
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        //서버 엔드포인트
        registry.addEndpoint("/chatEndpoint").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        //서버에서 -> 클라이언트
        registry.enableSimpleBroker("/topic");
        //클라이언트 -> 서버
        registry.setApplicationDestinationPrefixes("/app");
    }
}
