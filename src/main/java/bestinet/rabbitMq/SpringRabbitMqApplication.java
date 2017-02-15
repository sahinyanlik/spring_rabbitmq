package bestinet.rabbitMq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import bestinet.demandService.Receiver;

@SpringBootApplication(scanBasePackages={"bestinet.rabbitMq","bestinet.demandService"})
public class SpringRabbitMqApplication {
	
	final static String queueName = "spring-boot";
	
	
	@Bean
	Queue queue(){
		return new Queue(queueName,false);
	}
	
	@Bean
	TopicExchange exchange(){
		return new TopicExchange("spring-boot-exchange");
	}
	
	@Bean
	Binding binding (Queue queue, TopicExchange exchange){
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}
	
	
	@Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
	

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter, MessageConverter messageConverter){
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		container.setMessageConverter(messageConverter);
		return container;
	}
	
	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver){
       MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
       messageListenerAdapter.setMessageConverter(messageConverter());
       return messageListenerAdapter;
	}
	

	 @Bean
	 public MessageConverter messageConverter() {
	        ContentTypeDelegatingMessageConverter messageConverter = new ContentTypeDelegatingMessageConverter();
	        messageConverter.addDelegate("application/json", new Jackson2JsonMessageConverter());
	        return messageConverter;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringRabbitMqApplication.class, args);
	}
}
