package com.luosai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.*;

@SpringBootApplication
@EnableCaching
public class RedisdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisdemoApplication.class, args);
	}
	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,Map< MessageListenerAdapter, Collection<?extends Topic>> messageListeners,
											MessageListener helloAdapter,MessageListener worldAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(helloAdapter, new PatternTopic("hello"));
		container.addMessageListener(worldAdapter,new PatternTopic("world"));

//		container.setMessageListeners(messageListeners);
		return container;
	}
	@Bean
	MessageListenerAdapter helloAdapter(HelloHandle helloHandle) {
		return new MessageListenerAdapter(helloHandle, "handle");
	}
	@Bean
	MessageListenerAdapter worldAdapter(WorldHandle worldHandle) {
		return new MessageListenerAdapter(worldHandle, "handle");
	}
	@Bean
	Map< MessageListenerAdapter, Collection<?extends Topic>> messageListeners(MessageListenerAdapter helloAdapter,MessageListenerAdapter worldAdapter){
		Map<MessageListenerAdapter, Collection<?extends Topic>> map = new HashMap<>();
		map.put(helloAdapter, Arrays.asList(new PatternTopic("hello")));
		map.put(worldAdapter,Arrays.asList(new PatternTopic("world")));
		return map ;
	}
}
