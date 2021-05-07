package org.acme.client;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.WebServiceException;

import com.learnwebservices.services.hello.HelloEndpoint;
import com.learnwebservices.services.hello.HelloEndpointService;
import com.learnwebservices.services.hello.HelloRequest;
import com.learnwebservices.services.hello.HelloResponse;

@Path("/hello")
public class HelloResource {

	@Inject
	HelloEndpoint helloEndpoint;

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public HelloResponse sayHello(HelloRequest request) {
		System.out.println("POST /hello");
		return helloEndpoint.sayHello(request);
	}

	@POST
	@Path("/dispatch")
	@Consumes({ MediaType.TEXT_PLAIN })
	@Produces({ MediaType.TEXT_PLAIN })
	public String dispatch(String request) {
		System.out.println("POST /dispatch");
		HelloEndpointService service = new HelloEndpointService();

		Dispatch<Source> dispatch = service.createDispatch(HelloEndpointService.HelloEndpointPort, Source.class,
				Mode.MESSAGE);
		dispatch.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/cxf/helloendpoint");

		try {
			Source invoke = dispatch.invoke(new StreamSource(new StringReader(request)));

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

			ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
			transformer.transform(invoke, new StreamResult(streamOut));
			return streamOut.toString();
		} catch (WebServiceException | TransformerFactoryConfigurationError | TransformerException e) {
			return "Error " + e.toString();
		}
	}

}
