package com.yonyou.cloud.ops.notify.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yonyou.cloud.mom.client.producer.MqSenderDefaultImpl;
import com.yonyou.cloud.ops.notify.mq.listener.ThirdMessageListener;
import com.yonyou.cloud.track.Track;

@Configuration
public class MqConfig {
	@Bean
	public Queue pointsListenLoginQueue() {
		return new Queue("consumer-third-message", true); // 队列持久
	}

	@Bean
	public FanoutExchange eventExchange() {
		return new FanoutExchange("notify");
	}

	@Bean
	public Binding PointsBindingLogin() {
		return BindingBuilder.bind(pointsListenLoginQueue()).to(eventExchange());
//				.with("queue-key");
	}

	@Bean
	public SimpleMessageListenerContainer messageContainer1(ConnectionFactory connectionFactory,
			ThirdMessageListener listener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
		container.setQueues(pointsListenLoginQueue());
		container.setExposeListenerChannel(true);
		container.setMaxConcurrentConsumers(1);
		container.setConcurrentConsumers(1);
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 设置确认模式手工确认
		container.setMessageListener(listener);
		container.setMaxConcurrentConsumers(10);//设置最大消费者数量 防止大批量涌入
		return container;
	}
	
	@Bean
	public MessageConverter messageConverter() {
		JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();
		return jsonMessageConverter;
	}
	
	@Bean
	public MqSenderDefaultImpl mqSenderDefaultImpl(RabbitOperations rabbitOperations) {
		MqSenderDefaultImpl mqSenderDefaultImpl = new MqSenderDefaultImpl();
		mqSenderDefaultImpl.setRabbitOperations(rabbitOperations);
		return mqSenderDefaultImpl;
	}
	
	@Bean
	public ThirdMessageListener thirdMessageListener() {
		ThirdMessageListener thirdMessageListener = new ThirdMessageListener();
		return thirdMessageListener;
	}
	
//	@Bean
//	public Track track(ThirdMessageListener thirdMessageListener) {
//		Track trace = new Track(thirdMessageListener);
//		return trace;
//	}
	
}