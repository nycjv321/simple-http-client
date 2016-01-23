# Simple Http Client

A simple wrapper of Apache's Http Client Library that tries to make interacting with HTTP Easy

## Example Usage

    // Create a client that only returns the response body
    SimpleHttpClient simpleClient = SimpleClientBuilder.create().build();

    // Or create a Response Client that returns a responses whichs includes
    // both header and body information
    ResponseClient responseClient = ResponseClientBuilder.create().build();

    // Download Google's homepage
    simpleClient.get("http://google.com") 
    
    // Resolve JSON Objects (provided via org.json)
    simpleClient.getJSON("http://localhost:3000/example.json")
    
    // Resolve XML Document (provided via jdom)
    simpleClient.getDocument("http://localhost:3000/example.xml")
    
    // Returns a response object of the the requested url sans content
    simpleClient.head("http://localhost:3000/example.json") 
    
    // Put a resource with custom headers
    responseClient.put("http://localhost:3000/resources", "/tmp/resource.json", new BasicHeader("key", "value"))
	
    // Some as above with no headers
    responseClient.put("http://localhost:3000/resources", "/tmp/resource.json")

    // Post a resource with custom headers
    responseClient.post("http://localhost:3000/resource", "/tmp/resource.json", new BasicHeader("key", "value"))

	// POST to a resource with no body and multiple headers
    responseClient.post(
        "http://localhost:3000/resource", 
        new BasicHeader("key", "value"),
        new BasicHeader("anotherHeader", "anotherValue")
    ) 


### What about authentication?

    SimpleClientBuilder client = SimpleClientBuilder.create().credentialProvider(
        CredentialsProviderBuilder.BasicUserNamePasswordBuilder(
            "username", "password"
        )
    ).build();

## Why?

Sometimes I find myself needing to make a custom wrapper for a REST API while writing Java. 

## Notes

* All requests made with this library are appropriately closed. I've noticed most code 
examples of the Apache Http Client library found across the Internet don't actually 
close connections/io properly. This might explain a lot of things about other Java code
that interacts with Web APIs. :) If you look at the code in this repo, you will see that 
there is an attempt to (try to) eloquently close resources whenever necessary. For example:

    try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
      validateResponse(response, url);
      logger.debug(String.format("GET %s: %s", url, response.getStatusLine()));
      return consume(response); // Content is parsed 
    } catch (IOException e) { // connection is closed via try-with-resources
      logger.error(e);
    }
    
         
* This library wraps Apache Http Client 4.3.
* All requests are logged via Log4j to make debugging easier. For example:

    // [Request Type] [Resource Requested]: [Response Status Line]
    2015-11-19 00:29:55,770 DEBUG [main] http.SimpleHttpClient (SimpleHttpClient.java:202) - GET http://127.0.0.1:1080/: HTTP/1.1 200 OK
 
## Requirements
JDK 1.8

### Dependencies

Compile time dependencies listed below:

    [user@deathstar simple-http-client]$ mvn dependency:tree | grep -v 'test'
    [INFO] Scanning for projects...
    [INFO]                                                                         
    [INFO] ------------------------------------------------------------------------
    [INFO] Building com.nycjv321:simple-http-client 1.1-SNAPSHOT
    [INFO] ------------------------------------------------------------------------
    [INFO] 
    [INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ simple-http-client ---
    [INFO] com.nycjv321:simple-http-client:jar:1.1-SNAPSHOT
    [INFO] +- org.apache.httpcomponents:httpclient:jar:4.3:compile
    [INFO] |  +- org.apache.httpcomponents:httpcore:jar:4.3:compile
    [INFO] |  +- commons-logging:commons-logging:jar:1.1.3:compile
    [INFO] |  \- commons-codec:commons-codec:jar:1.6:compile
    [INFO] +- org.apache.logging.log4j:log4j-api:jar:2.1:compile
    [INFO] +- org.apache.logging.log4j:log4j-core:jar:2.1:compile
    [INFO] +- org.json:json:jar:20090211:compile
    [INFO] +- org.jdom:jdom:jar:2.0.2:compile
    [INFO] +- org.jetbrains:annotations:jar:15.0:compile
    [INFO] +- com.nycjv321:utilities:jar:1.4:compile
    [INFO] +- org.apache.commons:commons-lang3:jar:3.4:compile
    [INFO] +- commons-io:commons-io:jar:2.4:compile
    [INFO] \- com.google.guava:guava:jar:18.0:compile