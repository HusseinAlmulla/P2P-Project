package edu.polyu.comp.ws;

import java.io.InputStream;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.polyu.comp.domain.Transaction;
import edu.polyu.comp.service.TransactionService;
import edu.polyu.comp.util.StringUtil;

@Path("sendMoney")
public class P2pSendMoney {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response processInputRequest(InputStream requestBodyStream) {
		String output = "";

		try {
			output = StringUtil.convertInputStreamToString(requestBodyStream);
			
			// convert the incoming JSON message body into POJO
			ObjectMapper jackson = new ObjectMapper();
			Transaction tx = jackson.readValue(output, Transaction.class);
			
			// add JDBC call here to execute the tx in DBMS
			boolean result = new TransactionService().sendMoney(tx); // the result from DBMS is executed successfully			
			return Response.status(Response.Status.OK).entity("{\"result\":\"" + result + "\"}").build();			
		} catch (Exception e) {
			e.printStackTrace(); // log the error
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
