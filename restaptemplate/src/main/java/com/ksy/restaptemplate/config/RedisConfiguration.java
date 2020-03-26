package com.ksy.restaptemplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ksy.restaptemplate.chat.pubsub.RedisSubscriber;

import lombok.RequiredArgsConstructor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RequiredArgsConstructor
@Configuration
public class RedisConfiguration {
	 @Bean
	    public ChannelTopic channelTopic() {
	        return new ChannelTopic("chatroom");
	    }

	    /**
	     * redis에 발행(publish)된 메시지 처리를 위한 리스너 설정
	     */
	    @Bean
	    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory,
	                                                              MessageListenerAdapter listenerAdapter,
	                                                              ChannelTopic channelTopic) {
	        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
	        container.setConnectionFactory(connectionFactory);
	        container.addMessageListener(listenerAdapter, channelTopic);
	        return container;
	    }

	    /**
	     * 실제 메시지를 처리하는 subscriber 설정 추가
	     */
	    @Bean
	    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
	        return new MessageListenerAdapter(subscriber, "sendMessage");
	    }

	    /**
	     * 어플리케이션에서 사용할 redisTemplate 설정
	     */
	    @Bean
	    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
	        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	        redisTemplate.setConnectionFactory(connectionFactory);
	        redisTemplate.setKeySerializer(new StringRedisSerializer());
	        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
	        return redisTemplate;
	    }
}