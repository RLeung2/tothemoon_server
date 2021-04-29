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
		Path nevadaJobOnePath = Paths.get("C:\\Users\\Ahmed\\git\\tothemoon\\ToTheMoon\\src\\main\\java\\DistrictingData\\nv_d1000_c1000_r25_p10.json");
		Path nevadaJobTwoPath = Paths.get("C:\\Users\\Ahmed\\git\\tothemoon\\ToTheMoon\\src\\main\\java\\DistrictingData\\nv_d950_c1000_r25_p15.json");
		String nevadaJobOneFileName = nevadaJobOnePath.getFileName().toString();
		String nevadaJobTwoFileName = nevadaJobTwoPath.getFileName().toString();
		String[] nevadaJobOneStringParameters = nevadaJobOneFileName.split("_");
		String[] nevadaJobOnePopulationEqualityString = (nevadaJobOneStringParameters[4].substring(1, nevadaJobOneStringParameters[4].length())).split("\\.");
		float nevadaJobOnePopulationEquality = Float.parseFloat(nevadaJobOnePopulationEqualityString[0]);
		int nevadaJobOneRounds = Integer.parseInt(nevadaJobOneStringParameters[3].substring(1, nevadaJobOneStringParameters[3].length()));
		int nevadaJobOneCoolingPeriod = Integer.parseInt(nevadaJobOneStringParameters[2].substring(1, nevadaJobOneStringParameters[2].length()));
		int nevadaJobOneNumDistrictings = Integer.parseInt(nevadaJobOneStringParameters[1].substring(1, nevadaJobOneStringParameters[1].length()));
		String[] nevadaJobTwoStringParameters = nevadaJobTwoFileName.split("_");
		String[] nevadaJobTwoPopulationEqualityString = (nevadaJobTwoStringParameters[4].substring(1, nevadaJobTwoStringParameters[4].length())).split("\\.");
		float nevadaJobTwoPopulationEquality = Float.parseFloat(nevadaJobTwoPopulationEqualityString[0]);
		int nevadaJobTwoRounds = Integer.parseInt(nevadaJobTwoStringParameters[3].substring(1, nevadaJobTwoStringParameters[3].length()));
		int nevadaJobTwoCoolingPeriod = Integer.parseInt(nevadaJobTwoStringParameters[2].substring(1, nevadaJobTwoStringParameters[2].length()));
		int nevadaJobTwoNumDistrictings = Integer.parseInt(nevadaJobTwoStringParameters[1].substring(1, nevadaJobTwoStringParameters[1].length()));
		
		nevadaJobSummaries.add(new JobSummary(state, nevadaJobOneNumDistrictings, nevadaJobOnePopulationEquality, nevadaJobOneRounds, nevadaJobOneCoolingPeriod, nevadaJobOneFileName));
		nevadaJobSummaries.add(new JobSummary(state, nevadaJobTwoNumDistrictings, nevadaJobTwoPopulationEquality, nevadaJobTwoRounds, nevadaJobTwoCoolingPeriod, nevadaJobTwoFileName));
		
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
