package org.acme;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.XmlConfig;
import io.restassured.parsing.Parser;;

@QuarkusTest
public class HelloResourceTest {

// IS NOT WORKING - RESTEASY006515: The method createclass com.learnwebservices.services.hello.HelloResponse() was not found in the object Factory! 
//    @Test
//    public void testSayHelloEndpoint() {
//		given()
//			.header("Content-type", "application/json")
//			.and()
//			.body("{ \"name\": \"Joe\"}")
//			.when().post("/hello")
//			.then()
//				.statusCode(200)
//				.body(is("Hello Joe"));
//	}

    @Test
    public void testDispatch() {
    	RestAssured.registerParser("text/plain", Parser.XML);

		given()
			.config(RestAssuredConfig.config()
				.xmlConfig(XmlConfig.xmlConfig()
					.with()
						.namespaceAware(true)
						.declareNamespace("soap", "http://schemas.xmlsoap.org/soap/envelope/")))
			.header("Content-type", "text/plain")
			.and()
			.body("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header/><soapenv:Body><SayHello xmlns=\"http://learnwebservices.com/services/hello\"><HelloRequest><Name>John Doe</Name></HelloRequest></SayHello></soapenv:Body></soapenv:Envelope>")
			.when().post("/hello/dispatch")
			.then()
				.statusCode(200)
				.body("soap:Envelope.soap:Body.SayHelloResponse.HelloResponse.Message.text()", equalTo("Hello John Doe"));
	}
}
