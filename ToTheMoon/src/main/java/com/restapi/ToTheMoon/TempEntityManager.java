package com.restapi.ToTheMoon;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TempEntityManager {
	
	public TempEntityManager() {
		
	}
	
	public List<JobSummary> getNevadaJobSummaries(USState state) {
		List<JobSummary> nevadaJobSummaries = new ArrayList<JobSummary>();
		Path nevadaJobOnePath = Paths.get("C:\\Users\\Brian\\OneDrive\\Documents\\GitHub\\tothemoon_server\\ToTheMoon\\src\\main\\java\\DistrictingData\\nv_c1000_r25_p10.json");
		Path nevadaJobTwoPath = Paths.get("C:\\Users\\Brian\\OneDrive\\Documents\\GitHub\\tothemoon_server\\ToTheMoon\\src\\main\\java\\DistrictingData\\nv_c1000_r25_p15.json");
		String nevadaJobOneFileName = nevadaJobOnePath.getFileName().toString();
		String nevadaJobTwoFileName = nevadaJobTwoPath.getFileName().toString();
		String[] nevadaJobOneStringParameters = nevadaJobOneFileName.split("_");
		float nevadaJobOnePopulationEquality = Float.parseFloat(nevadaJobOneStringParameters[3].substring(1, nevadaJobOneStringParameters[3].length()));
		int nevadaJobOneRounds = Integer.parseInt(nevadaJobOneStringParameters[2].substring(1, nevadaJobOneStringParameters[2].length()));
		int nevadaJobOneCoolingPeriod = Integer.parseInt(nevadaJobOneStringParameters[1].substring(1, nevadaJobOneStringParameters[1].length()));
		String[] nevadaJobTwoStringParameters = nevadaJobTwoFileName.split("_");
		float nevadaJobTwoPopulationEquality = Float.parseFloat(nevadaJobTwoStringParameters[3].substring(1, nevadaJobTwoStringParameters[3].length()));
		int nevadaJobTwoRounds = Integer.parseInt(nevadaJobTwoStringParameters[2].substring(1, nevadaJobTwoStringParameters[2].length()));
		int nevadaJobTwoCoolingPeriod = Integer.parseInt(nevadaJobTwoStringParameters[1].substring(1, nevadaJobTwoStringParameters[1].length()));
		
		nevadaJobSummaries.add(new JobSummary(state, 1000, nevadaJobOnePopulationEquality, nevadaJobOneRounds, nevadaJobOneCoolingPeriod, nevadaJobOneFileName));
		nevadaJobSummaries.add(new JobSummary(state, 1000, nevadaJobTwoPopulationEquality, nevadaJobTwoRounds, nevadaJobTwoCoolingPeriod, nevadaJobTwoFileName));
		
		return nevadaJobSummaries;
	}
	
//	TODO
//	public List<JobSummary> getNewYorkJobSummaries(USState state) {
//	
//	}
//	
//	TODO
//	public List<JobSummary> getWashingtonJobSummaries(USState state) {
//		
//	}
	
	public State getState(USState state) {
		Districting enactedDistricting = new Districting(-1, null, null);
		List<JobSummary> stateJobSummaries = getNevadaJobSummaries(state);
		
		return new State(state, enactedDistricting, stateJobSummaries);
	}
	
	public Job getJob() {
		return new Job();
	}
}
