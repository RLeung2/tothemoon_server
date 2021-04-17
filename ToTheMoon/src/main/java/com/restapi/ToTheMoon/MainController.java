package com.restapi.ToTheMoon;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.*;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


/** Exact path to the MainController is given by:
 * localhost:8080/ToTheMoon/webresources/tothemoon
 */
@Path("/tothemoon")
public class MainController {
	
	@GET
    @Path("/{state}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleSelectState(@PathParam("state") String state) {

        String greeting = "State selected: " + state;
        return Response.ok(greeting).build();
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleConstrainDistrictings(String jsonInput) {
		
        String greeting = "Constraints selected: " + jsonInput;
        return Response.ok(greeting).build();
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
