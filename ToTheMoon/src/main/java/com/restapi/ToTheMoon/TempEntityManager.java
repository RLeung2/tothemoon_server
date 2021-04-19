package com.restapi.ToTheMoon;

import java.util.ArrayList;
import java.util.List;

public class TempEntityManager {
	
	public TempEntityManager() {
		
	}
	
	public JobSummary getJobSummaries(USState state, int id) {
		return new JobSummary(state, 5000 + (int)(Math.random() * 5000), 
				(float)(Math.random() * 3), 10000, 10000, id);
	}
	
	public State getState(USState state) {
		Districting enactedDistricting = new Districting(0, null, null);
		List<JobSummary> jobSummaries = new ArrayList<JobSummary>();
		
		jobSummaries.add(getJobSummaries(state, 1));
		jobSummaries.add(getJobSummaries(state, 2));
		jobSummaries.add(getJobSummaries(state, 3));
		
		return new State(state, enactedDistricting, jobSummaries);
	}
	
	public Job getJob() {
		return new Job();
	}
}
