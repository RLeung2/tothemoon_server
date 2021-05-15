package com.restapi.ToTheMoon;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import Database.DatabaseJob;
import Database.DatabaseState;

import org.json.simple.parser.ParseException;

public class TempEntityManager {
	// private static final String NEVADA_ENACTED_FILE = "D:\\\\Users\\\\Documents\\\\GitHub\\\\tothemoon_server\\\\ToTheMoon\\\\src\\\\main\\\\java\\\\DistrictingData\\\\nv_districts_with_data.json";
	// private static final String NEVADA_GEO_FILE = "D:\\\\Users\\\\Documents\\\\GitHub\\\\tothemoon_server\\\\ToTheMoon\\\\src\\\\main\\\\java\\\\DistrictingData\\\\nv_geometry.json";
	
	private static final String NEVADA_ENACTED_FILE = Constants.YOUR_DIRECTORY_PREFIX + Constants.NEVADA_ENACTED_FILE_NAME;
	private static final String NEVADA_GEO_FILE = Constants.YOUR_DIRECTORY_PREFIX + Constants.NEVADA_GEOMETRY_FILE_NAME;

	
	private final String PERSISTENCE_UNIT_NAME = "ToTheMoon";
	private EntityManagerFactory factory;
	
	
	public TempEntityManager() {
		this.factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//		this.factory = new PersistenceProvider().createEntityManagerFactory("ToTheMoon");
	}
	
	public List<JobSummary> getNevadaJobSummaries(USState state) {
		List<JobSummary> nevadaJobSummaries = new ArrayList<JobSummary>();
		Path nevadaJobOnePath = Paths.get("D:\\Users\\Documents\\GitHub\\tothemoon_server\\ToTheMoon\\src\\main\\java\\DistrictingData\\nv_d1000_c1000_r25_p10.json");
		Path nevadaJobTwoPath = Paths.get("D:\\Users\\Documents\\GitHub\\tothemoon_server\\ToTheMoon\\src\\main\\java\\DistrictingData\\nv_d950_c1000_r25_p15.json");
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
	
	public State getState(USState state) throws FileNotFoundException, IOException, ParseException {
		//Districting enactedDistricting = new Districting(-1, null, null);
		List<JobSummary> stateJobSummaries = getNevadaJobSummaries(state);
		State stateObject = new State();
		stateObject.setCurrState(state);
		stateObject.setJobSummaries(stateJobSummaries);
		
		return stateObject;
		//return new State(state, enactedDistricting, stateJobSummaries);
	}
	
	public Job getJob() {
		return new Job();
	}
	
	public void instantiateStatesandJobs() {
		EntityManager em = this.factory.createEntityManager();
        em.getTransaction().begin();
        
        DatabaseState nevada = new DatabaseState("NV");
        DatabaseState southCarolina = new DatabaseState("SC");
        DatabaseState washington = new DatabaseState("WA");
        DatabaseJob nevadaJob = new DatabaseJob();
        DatabaseJob southCarolinaJob = new DatabaseJob();
        DatabaseJob washingtonJob = new DatabaseJob();
        
        nevadaJob.setJobState(nevada);
        nevadaJob.setJobFilePath("nv_d1000_c1000_r25_p10.json");
        
        southCarolinaJob.setJobState(southCarolina);
        southCarolinaJob.setJobFilePath("sc_d1000_c1020_r26_p11.json");
        
        washingtonJob.setJobState(washington);
        washingtonJob.setJobFilePath("wa_d1000_c1030_r27_p12.json");
        
		em.persist(nevada);
		em.persist(southCarolina);
		em.persist(washington);
		
		em.persist(nevadaJob);
		em.persist(southCarolinaJob);
		em.persist(washingtonJob);
		
		em.getTransaction().commit();

        em.close();
	}
}
