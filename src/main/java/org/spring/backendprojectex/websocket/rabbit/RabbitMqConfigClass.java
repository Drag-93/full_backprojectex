package org.spring.backendprojectex.websocket.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableRabbit
public class RabbitMqConfigClass {

    private final ConnectionFactory connectionFactory;

    @Value("${rabbitmq.exchange.name}")
    private String topicExchangeName;

    @Value("${rabbitmq.queue.name}")
    private String queueName;
    @Value("${rabbitmq.question.routing.key}")
    private String routingKey;

    @Bean
    Queue queue(){
        return new Queue(queueName,false);
    }
    @Bean
    TopicExchange exchange(){
        return new TopicExchange(topicExchangeName);
    }
    @Bean
    Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    //RabbitMQ메시지를 수신하기 위한 컨테이너를 생성
    //각 리스너의 독립적인 환경 제공
    //자동으로 설정
    @Bean
    public SimpleRabbitListenerContainerFactory myFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer){
        SimpleRabbitListenerContainerFactory factory= new SimpleRabbitListenerContainerFactory();
        System.out.println(factory+"<<factory");
        configurer.configure(factory,connectionFactory);
        return factory;
    }
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter){
        SimpleMessageListenerContainer container=new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver){
        MessageListenerAdapter listenerAdapter= new MessageListenerAdapter(receiver,"receiveMessage");
        listenerAdapter.setMessageConverter(messageConverter());
        return listenerAdapter;
    }

    //JSON으로 자동 변경
    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}

