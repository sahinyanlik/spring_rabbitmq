package bestinet.rabbitMq;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import bestinet.demandService.Receiver;


@Component
public class Runner implements CommandLineRunner  {

	private final RabbitTemplate rabbitTemplate;
	
	private final ConfigurableApplicationContext context;
	private Receiver receiver;
	
	private final MessageConverter messageConverter;
	
	public Runner(Receiver receiver, RabbitTemplate rabbitTemplate, ConfigurableApplicationContext context,MessageConverter messageConverter){
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
		this.context = context;
		this.messageConverter = messageConverter;
		
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		System.out.println("Sending Messsage");
		String jsonString = "{ \"id\": \"TestName\", \"name\": \"UK\" }";
		IReceiver i = new IReceiver();
		i.setId("5");
		i.setName("Test");
		
		String gson = new Gson().toJson(i);
		

		//rabbitTemplate.convertAndSend(SpringRabbitMqApplication.queueName,"t");
		rabbitTemplate.convertAndSend(SpringRabbitMqApplication.queueName,buildMessage(jsonString,messageConverter));
		receiver.getLatch().await(1000,TimeUnit.MILLISECONDS);
		context.close();
	}
	
	 public static  Message buildMessage(Object object, MessageConverter messageConverter) {
	        MessageProperties messageProperties = MessagePropertiesBuilder.newInstance()
	                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
	                .build();
	        return messageConverter.toMessage(object, messageProperties);
	}
	
	
	


}
