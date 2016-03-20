package edu.polyu.comp.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Plain old Java Object it does not extend as class or implements 
//an interface

//The class registers its methods for the HTTP GET request using the @GET annotation. 
//Using the @Produces annotation, it defines that it can deliver several MIME types,
//text, XML and HTML. 

//The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello

@Path("/hello")
public class Hello {
	
	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response sayHelloWorld() {
		String output = "Hello World!";
		return Response.status(200).entity(output).build();
	}
	
	// This method is called if HTML is request
	// The browser will always request the HTML MIME type
	@GET
	@Path("/{name}")
	@Produces(MediaType.TEXT_HTML)
	public Response sayHtmlHello(@PathParam("name") String name) {
		String output = "<html> " + "<title>" + "Hello " + name + "</title>" + "<body><h1>" + "Hello " + name
				+ "</body></h1>" + "</html> ";
		return Response.status(200).entity(output).build();
	}
	
	// This method is called if XML is request
	@GET
	@Path("/{name}")
	@Produces(MediaType.TEXT_XML)
	public Response sayXMLHello(@PathParam("name") String name) {
		String output = "<?xml version=\"1.0\"?>" + "<hello> Hello " + name + "</hello>";
		return Response.status(200).entity(output).build();
	}
	
	// This method is called if JSON is request
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sayJSONHello(@PathParam("name") String name) {
		String output = "{\"Hello\":\"" + name + "\"}";
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/{name}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response sayHello(@PathParam("name") String name) {
		String output = "Hello, " + name;
		return Response.status(200).entity(output).build();
	}
}
