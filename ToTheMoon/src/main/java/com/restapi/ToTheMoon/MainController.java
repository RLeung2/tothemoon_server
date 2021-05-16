package com.restapi.ToTheMoon;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import javafx.util.Pair;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import Database.DatabaseState;

/** Exact path to the MainController is given by:
 * localhost:8080/ToTheMoon/webresources/tothemoon
 */
@Path("/tothemoon")
public class MainController {
	private TempEntityManager em = new TempEntityManager();
	private static final String NEVADA_ENACTED_FILE = "D:\\\\Users\\\\Documents\\\\GitHub\\\\tothemoon_server\\\\ToTheMoon\\\\src\\\\main\\\\java\\\\DistrictingData\\\\nv_districts_with_data.json";
	private static final String NEVADA_GEO_FILE = Constants.YOUR_DIRECTORY_PREFIX + Constants.NEVADA_GEOMETRY_FILE_NAME;
	
	private TempEntityManager entityManager = new TempEntityManager();
	private State currState;
	private Job currJob;
	
	@GET
    @Path("/{state}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleSelectState(@PathParam("state") String state, 
    		@Context HttpServletRequest req) throws FileNotFoundException, IOException, ParseException {
		HttpSession session = req.getSession(true);
		ObjectMapper mapper = new ObjectMapper();
		
		USState stateEnum = UserInputToEnumTransformer.transformUserStateToEnum(state);
		State currState = em.getState(stateEnum);

		currState.generateEnactedDistricting(Constants.YOUR_DIRECTORY_PREFIX + Constants.SC_ENACTED_FILE_NAME, 
				Constants.YOUR_DIRECTORY_PREFIX + Constants.SC_JOB_GEOMETRY_FILE_NAME);
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
	
	@GET
    @Path("/databasetest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleDatabaseTest() {
		ObjectMapper mapper = new ObjectMapper();
//		JobSummary jobSummary = em.getInitialJobSummary(USState.WA, 2);
		List<String> jobPaths = em.getJobNumberJobPaths(USState.WA, 1);
//		List<String> entitiesToStringList = em.instantiateStatesandJobs();
//		List<JobSummary> summaries = em.getJobSummaries(USState.SC);
//		for (JobSummary summary : summaries) {
//			System.out.println(summary.getCoolingPeriod());
//			System.out.println(summary.getJobId());
//			System.out.println(summary.getNumDistrictings());
//			System.out.println(summary.getPopulationEquality());
//			System.out.println(summary.getRecom());
//			System.out.println(summary.getRounds());
//		}
		
		System.out.println(em.getEnactedGeometry(USState.WA));
		System.out.println(em.getPrecinctGeometry(USState.WA));
		
		try {
			String entitiesToStringJSON = mapper.writeValueAsString(jobPaths);
	        return Response.ok(entitiesToStringJSON).build();
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
	
	@POST
    @Path("/constrainJob/{minority}")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleConstrainDistrictings(@PathParam("minority") String input, String body,
    		@Context HttpServletRequest req) throws FileNotFoundException, IOException, ParseException, InterruptedException {
		ObjectMapper mapper = new ObjectMapper();
		HttpSession session = req.getSession();
		
		String GEO_FILE = Constants.YOUR_DIRECTORY_PREFIX + Constants.SC_JOB_GEOMETRY_FILE_NAME;
		
		MinorityPopulation minority = UserInputToEnumTransformer.transformUserMinorityPopToEnum(input);
		State currState = (State) session.getAttribute("currState");
		Districting enactedDistricting = currState.getEnactedDistricting();
        Job testJob = new Job();
        testJob.setCurrMinorityPopulation(minority);
        testJob.setEnactedDistricting(enactedDistricting);
        testJob.generatePrecinctPopulationMap(GEO_FILE);
        
		int counter = 0;
		String jobFileName = Constants.YOUR_DIRECTORY_PREFIX + Constants.SC_JOB_90000_FILE_NAME;

		ArrayList<Districting> districtings = new ArrayList<>();
		
		// TODO -- change this with a request from the DB or sumn
//		String[] fileNamesArr = {"sc_c1000_r500_p10.json", "sc_c1000_r500_p20.json", "sc_c2000_r500_p10.json",
//				"sc_c2000_r500_p20.json", "sc_c3000_r500_p10.json", "sc_c3000_r500_p20.json", 
//				"sc_c4000_r500_p20.json", "sc_c500_r500_p10.json", "sc_c500_r500_p20.json"};
		
		// String[] fileNamesArr = {Constants.NEVADA_JOB_10000_FILE_NAME};
		String[] fileNamesArr = {"sc_c1000_r500_p10.json"};
		
		
		ConstraintsJSON requestBody = mapper.readValue(body, ConstraintsJSON.class);
		String popEqScoreType = "";
		String minorityPopPercentageType = "";
		if (requestBody.popType.equals("VAP")) {
			popEqScoreType = "tvapScore";
			minorityPopPercentageType = input.substring(0,1).toUpperCase() + "TPERCENTAGE";
		} else {
			popEqScoreType = "totalPopulationScore";
			minorityPopPercentageType = input.substring(0,1).toUpperCase() + "PERCENTAGE";
		}
		
		List<ConstrainerThread> threadList = new ArrayList<ConstrainerThread>();
		for (int i = 0; i < fileNamesArr.length; i++) {
			ConstrainerThread cThread = new ConstrainerThread(fileNamesArr[i], 10000 * i, testJob, requestBody.popEq, popEqScoreType, 
					requestBody.popType, requestBody.compactness, requestBody.mmThreshhold, requestBody.numMM, requestBody.incumbents, minorityPopPercentageType);
			cThread.start();
			threadList.add(cThread);
		}
		
	    for(ConstrainerThread t : threadList) {
	        t.join();
	    }
	    
	    int idNums = 0;
	    for(ConstrainerThread t : threadList) {
	    	List<Districting> dList = t.getDistrictingsList();
	        for (int i = 0; i < dList.size(); i++) {
	        	Districting districting = dList.get(i);
	        	districting.setId(idNums);
	        	idNums++;
	        	districtings.add(districting);
	        }
	    }
        
        testJob.setDistrictings(districtings);
        
        this.currJob = testJob;
        session.setAttribute("currJob", this.currJob);
        
		try {
			String numLeft = mapper.writeValueAsString(districtings.size());
	        return Response.ok(numLeft).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
    }
	
	
	@POST
    @Path("/measures")
	//@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleSetMeasures(String jsonInput, @Context HttpServletRequest req) throws JsonGenerationException, JsonMappingException, IOException {
		HttpSession session = req.getSession();
		Job job = (Job) session.getAttribute("currJob");
		
		job.generateBoxAndWhiskerData();
		job.findAverageDistricting();
		job.applyMeasures(1, 1, 1, 1, 1);
		job.generateDistrictingAnalysisSummary();
        job.renumberDistrictings();
        job.generateBoxAndWhiskerData();
        
        DistrictingAnalysisSummary summary = job.getDistrictingAnalysisSummary();
        Districting dis = job.getDistrictingAtIndex(0);
        ObjectMapper mapper = new ObjectMapper();
		try {
			String summaryJson = mapper.writeValueAsString(summary);
	        return Response.ok(summaryJson).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
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
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@GET
    @Path("/districting/{districtingIndex}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleGetDistricting(@PathParam("districtingIndex") String index, 
    		@Context HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		Job job = (Job) session.getAttribute("currJob");
		Districting districting = job.getDistrictingAtIndex(Integer.parseInt(index));
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String districtingJSON = mapper.writeValueAsString(districting);
	        return Response.ok(districtingJSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@GET
    @Path("/gillConstruct/{districtingIndex}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleGillConstruct(@PathParam("districtingIndex") String index, 
    		@Context HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		Job job = (Job) session.getAttribute("currJob");
		State currState = (State) session.getAttribute("currState");
		Districting enactedDistricting = currState.getEnactedDistricting();
		Districting districting = job.getDistrictingAtIndex(Integer.parseInt(index));
		
		districting.gillConstructRenumbering(enactedDistricting);		
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String districtingJSON = mapper.writeValueAsString(districting);
	        return Response.ok(districtingJSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@GET
    @Path("/megaTest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response megaTest(@PathParam("districtingIndex") String index, 
    		@Context HttpServletRequest req) throws FileNotFoundException, IOException, ParseException {
		
		State stateObject = new State();
		stateObject.setCurrState(USState.NV);
		stateObject.generateEnactedDistricting(NEVADA_ENACTED_FILE, NEVADA_GEO_FILE);
		
		Districting enactedDistricting = stateObject.getEnactedDistricting();
        Job testJob = new Job();
        testJob.setCurrMinorityPopulation(MinorityPopulation.HISPANIC);
        testJob.setEnactedDistricting(enactedDistricting);
        testJob.generatePrecinctPopulationMap(NEVADA_GEO_FILE);
        testJob.fillDistrictings();
        testJob.findAverageDistricting();
        testJob.generateDistrictingAnalysisSummary();
        DistrictingAnalysisSummary testSummary = testJob.getDistrictingAnalysisSummary();
        testJob.renumberDistrictings();
        testJob.generateBoxAndWhiskerData();
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String testJSON = mapper.writeValueAsString(testSummary);
	        return Response.ok(testJSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@GET
    @Path("/updateTest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleUpdate( @Context HttpServletRequest req) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			String districtingJSON = mapper.writeValueAsString(new ObjectiveFunction());
	        return Response.ok(districtingJSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
