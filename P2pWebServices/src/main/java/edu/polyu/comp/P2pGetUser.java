package edu.polyu.comp;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("getUser")
public class P2pGetUser {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response processInputRequest(InputStream requestBodyStream) {
		String output = "";

		try {
			output = StringUtil.convertInputStreamToString(requestBodyStream);
			
			// convert the incoming JSON message body into POJO
			ObjectMapper jackson = new ObjectMapper();
			User user = jackson.readValue(output, User.class);
			
			// add JDBC call here to retrieve the user name from DBMS using the phone as primary key
			String phone = user.getPhone();
			String name = "jeffrey"; // return empty string if not found from DBMS
			
			return Response.status(Response.Status.OK).entity("{\"name\":\"" + name + "\"}").build();
			
		} catch (Exception e) {
			e.printStackTrace(); // log the error
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
