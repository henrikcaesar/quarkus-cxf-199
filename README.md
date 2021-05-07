# quarkus-cxf-199

Reproducing https://github.com/quarkiverse/quarkus-cxf/issues/199

The application exposes a REST interface that forwards the requests to a SOAP service.

Running with `mvn verify` works fine, however `mvn verify -Pnative` does not.

The test uses the dispatch method instead of calling the sayHello method because of another fault:

	RESTEASY006515: The method createclass com.learnwebservices.services.hello.HelloResponse() was not found in the object Factory!
