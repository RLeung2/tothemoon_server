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

import com.fasterxml.jackson.core.JsonParseException;

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
	private Job currJob;
	
	@GET
    @Path("/{state}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleSelectState(@PathParam("state") String state, @Context HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		ObjectMapper mapper = new ObjectMapper();
		
		USState stateEnum = UserInputToEnumTransformer.transformUserStateToEnum(state);
		State currState = em.getState(stateEnum);
		
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
    @Path("/{state}/geojson")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleSendStateGeoJson(@PathParam("state") String state) {
		String stateGeoJsonFilePath = UserInputToEnumTransformer.transformUserStateToStateGeoJsonFilePath(state);
		ObjectMapper objectMapper = new ObjectMapper();
		
		Object object;
		try {
			object = new JSONParser().parse(new FileReader(stateGeoJsonFilePath));
		} catch (Exception e1) {
			e1.printStackTrace();
			return Response.serverError().build();
		}
		
		JSONObject jsonObject = (JSONObject) object;
		try {
			String stateGeoJSON = objectMapper.writeValueAsString(jsonObject);
	        return Response.ok(stateGeoJSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
    }
	
	//TODO
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
    public Response handleConstrainDistrictings(@PathParam("minority") String input, @Context HttpServletRequest req) {
		HttpSession session = req.getSession();
		MinorityPopulation minority = UserInputToEnumTransformer.transformUserMinorityPopToEnum(input);
		
        Job testJob = new Job();
        testJob.setCurrMinorityPopulation(minority);
        try {
			testJob.fillDistrictings();
		} catch (Exception e1) {
			e1.printStackTrace();
			return Response.serverError().build();
		}
        testJob.generateBoxAndWhiskerData();
        
        this.currJob = testJob;
        session.setAttribute("currJob", this.currJob);
		
        // TODO - random number now; change once constrain() is implemented
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
	
	// TODO - not implemented yet
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
    public Response handleGetBoxAndWhisker(@PathParam("districtingIndex") String input, @Context HttpServletRequest req) {
		HttpSession session = req.getSession();
		Job job = (Job) session.getAttribute("currJob");
		BoxAndWhisker boxAndWhisker = job.getBoxAndWhisker();
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleDisplayDistricting(@PathParam("districtingIndex") String index, @Context HttpServletRequest req) {
        HttpSession session = req.getSession();
		Job job = (Job) session.getAttribute("currJob");
		
		try {
			String responseJSON = job.generateDistrictingGeometry(Integer.parseInt(index));
	        return Response.ok(responseJSON).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}
	
}
