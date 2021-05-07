package org.acme.server;

import com.learnwebservices.services.hello.HelloEndpoint;
import com.learnwebservices.services.hello.HelloRequest;
import com.learnwebservices.services.hello.HelloResponse;

public class HelloEndpointImpl implements HelloEndpoint {

	@Override
	public HelloResponse sayHello(HelloRequest helloRequest) {
		System.out.println("Hello " + helloRequest.getName());
		HelloResponse helloResponse = new HelloResponse();
		helloResponse.setMessage("Hello " + helloRequest.getName());
		return helloResponse;
	}

}
