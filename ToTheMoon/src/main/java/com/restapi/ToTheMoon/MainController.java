package com.restapi.ToTheMoon;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/** Exact path to the MainController is given by:
 * localhost:8080/ToTheMoon/webresources/tothemoon
 */
@Path("/tothemoon")
public class MainController {
	
	private TempEntityManager em = new TempEntityManager();
	private State currState;
	
	@GET
    @Path("/{state}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleSelectState(@PathParam("state") String state, 
    		@Context HttpServletRequest req) {
		
		HttpSession session = req.getSession(true);
		
		USState stateEnum = UserInputToEnumTransformer.userStateToEnum(state);
		State currState = em.getState(stateEnum);
		this.currState = currState;
		
		session.setAttribute("currState", this.currState);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String stateJSON = mapper.writeValueAsString(currState);
	        return Response.ok(stateJSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
    }
	
	@GET
    @Path("/job/{jobID}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleJob(@PathParam("jobID") String jobID) {

        String greeting = "Job selected: " + jobID;
        return Response.ok(greeting).build();
    }
	
	@GET
    @Path("/constrainJob")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleConstrainDistrictings(String jsonInput) {
		
		int numberDistrictingsLeft = 900 + (int)(Math.random() * 150);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String numLeft = mapper.writeValueAsString(numberDistrictingsLeft);
	        return Response.ok(numLeft).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
    }
	
	@GET
    @Path("/measures")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleSetMeasures(String jsonInput) {
		
        String greeting = "Measures selected: " + jsonInput;
        return Response.ok(greeting).build();
	}
	
	@GET
    @Path("/districting/{districtingIndex}/boxAndWhisker")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleGetBoxAndWhisker(String jsonInput) {
		
        String greeting = "Box and Whisker for district #: " + jsonInput;
        return Response.ok(greeting).build();
	}
	
	@GET
    @Path("/districting/{districtingIndex}/display")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleDisplayDistricting(String jsonInput) {
		
        String greeting = "District to display: " + jsonInput;
        return Response.ok(greeting).build();
	}
}
