package bestinet.demandService;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ReceiverPojo {

	private String id;
	private String name;
	
	@JsonCreator
	public ReceiverPojo() {
	
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
}
