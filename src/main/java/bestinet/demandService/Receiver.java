package bestinet.demandService;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import bestinet.rabbitMq.IReceiver;

@Component
public class Receiver {
	
	private CountDownLatch latch = new CountDownLatch(1);
	
	public void receiveMessage(String p){
		IReceiver g = new Gson().fromJson(p, IReceiver.class);
		System.out.println(g.getId());
		latch.countDown();
	}
	
	public CountDownLatch getLatch(){
		return latch;
	}
	
}
