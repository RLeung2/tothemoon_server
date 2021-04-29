package com.restapi.ToTheMoon;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/** Exact path to the MainController is given by:
 * localhost:8080/ToTheMoon/webresources/tothemoon
 */
@Path("/tothemoon")
public class MainController {
	
	private TempEntityManager entityManager = new TempEntityManager();
	private State currState;
	private Job currJob;
	
	@GET
    @Path("/{state}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleSelectState(@PathParam("state") String state, @Context HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		USState stateEnum = UserInputToEnumTransformer.transformUserStateToEnum(state);
		State currState = entityManager.getState(stateEnum);
		ObjectMapper mapper = new ObjectMapper();
		
		this.currState = currState;
		session.setAttribute("currState", this.currState);
		
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
    public Response handleJob(@PathParam("jobID") String jobID, @Context HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		this.currState = new State(null, null, null);
		this.currState.setCurrState(USState.NV);
		session.setAttribute("job", this.currState);
		
        String greeting = "Job selected: " + jobID;
        return Response.ok(greeting).build();
    }
	
	@GET
    @Path("/constrainJob/{minority}")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleConstrainDistrictings(String input,
    		@Context HttpServletRequest req) throws FileNotFoundException, IOException, ParseException {
		
		HttpSession session = req.getSession();
		
		MinorityPopulation minority = UserInputToEnumTransformer.userMinorityPopToEnum(input);
        Job testJob = new Job();
        testJob.setCurrMinorityPopulation(minority);
        testJob.fillDistrictings();
        testJob.generateBoxAndWhiskerData();
        
        this.currJob = testJob;
        session.setAttribute("currJob", this.currJob);
		
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
	//@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleSetMeasures(String jsonInput, @Context HttpServletRequest req) throws JsonGenerationException, JsonMappingException, IOException {
		HttpSession session = req.getSession(true);
		State sesh = (State) session.getAttribute("job");
		
		
		ObjectMapper mapper = new ObjectMapper();
		String stateInfo = mapper.writeValueAsString(sesh);
		
        String greeting = "Measures selected: " + jsonInput;
        return Response.ok(stateInfo).build();
	}
	
	@GET
    @Path("/districting/{districtingIndex}/boxAndWhisker")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleGetBoxAndWhisker(@PathParam("districtingIndex") String input,
    		@Context HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		Job job = (Job) session.getAttribute("currJob");
		
		BoxAndWhisker boxAndWhisker = job.getBoxAndWhisker();
		//BoxAndWhisker boxAndWhisker = this.currJob.getBoxAndWhisker();
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String boxAndWhiskerJSON = mapper.writeValueAsString(boxAndWhisker);
	        return Response.ok(boxAndWhiskerJSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@GET
    @Path("/districting/{districtingIndex}/display")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleDisplayDistricting(String jsonInput, @Context HttpServletRequest req) {
		
        String greeting = "District to display: " + jsonInput;
        return Response.ok(greeting).build();
	}
	
}
