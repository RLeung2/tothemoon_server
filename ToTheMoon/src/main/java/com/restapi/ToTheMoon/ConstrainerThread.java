package com.restapi.ToTheMoon;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class ConstrainerThread extends Thread {
	private String fileName;
	private ArrayList<Districting> districtingsList;
	private int counter;
	private Job job;
	
	public ConstrainerThread(String fileName, int counter, Job job) {
		this.fileName = fileName;
		this.districtingsList = new ArrayList<>();
		this.counter = counter;
		this.job = job;
	}

	@Override
	public void run() {
	    try (
	            InputStream inputStream = Files.newInputStream(java.nio.file.Path.of(Constants.YOUR_DIRECTORY_PREFIX + this.fileName));
	            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
	    ) {            
	        reader.beginObject();
	        reader.nextName();
	        reader.beginArray();
	        while (reader.hasNext()) {
	        	JsonObject plan = new Gson().fromJson(reader, JsonObject.class);
	        	//Districting validPlan = testJob.constrain(0.15, "totalPopulationScore", "VAP", 0.02, 0.20, 2, "HPERCENTAGE", Arrays.asList(1), plan, counter);
	        	Districting validPlan = this.job.constrain(0.13, "totalPopulationScore", "VAP", 0.03, 0.20, 5, "BPERCENTAGE", Arrays.asList(4,6,7), plan, this.counter);
	        	if (validPlan != null) {
	        		this.districtingsList.add(validPlan);
	        	}
	        }
	        reader.endArray();
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Thread " + counter + " done.");
		}
	}
	
	public ArrayList<Districting> getDistrictingsList() {
		return districtingsList;
	}

}
