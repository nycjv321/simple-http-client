# Simple Http Client

A simple wrapper of Apache's Http Client Library that tries to make interacting with HTTP Easy

## Example Usage

    // Create the client
    SimpleHttpClient client = SimpleHttpClientBuilder.create().build();
    
    // Download Google's homepage
    client.get("http://google.com") 
    
    // Resolve JSON Objects (provided via org.json)
    client.getJSON("http://localhost:3000/example.json")
    
    // Resolve XML Document (provided via jdom)
    client.getDocument("http://localhost:3000/example.xml")
    
    // Returns a response object of the the requested url sans content
    client.head("http://localhost:3000/example.json") 
    
    // Put a resource with custom headers
    client.put("http://localhost:3000/resources", "/tmp/resource.json", new BasicHeader("key", "value")) 
	
    // Some as above with no headers
    client.put("http://localhost:3000/resources", "/tmp/resource.json") 

    // Post a resource with custom headers
    client.post("http://localhost:3000/resource", "/tmp/resource.json", new BasicHeader("key", "value")) 

	// POST to a resource with no body and multiple headeres
    client.post(
        "http://localhost:3000/resource", 
        new BasicHeader("key", "value"),
        new BasicHeader("anotherHeader", "anotherValue")
    ) 


### What about authentication?

    SimpleHttpClient client = SimpleHttpClientBuilder.create().credentialProvider(
        CredentialsProviderBuilder.BasicUserNamePasswordBuilder(
            "username", "password"
        )
    ).build();

## Why?

Sometimes I find myself needing to make a custom wrapper for a REST API while writing Java. 

## Notes

* All requests made with this library are appropriately closed. I noticed most code examples of the Apache Http Client library found accross the Internet don't actually close connections/io properly. This might explain alot of things about other Java code that interacts with Web APIs. :) If you look at the code in this repo, you will see that there is an attempt to (try to) eloquently close resources whenever necassary. For example:


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