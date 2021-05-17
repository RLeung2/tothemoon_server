package com.restapi.ToTheMoon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import Database.DatabaseEnacted;
import Database.DatabaseJob;
import Database.DatabaseState;

import org.json.simple.parser.ParseException;

public class TempEntityManager {	
	
	public TempEntityManager() {
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
		
		nevadaJobSummaries.add(new JobSummary(state, nevadaJobOneNumDistrictings, nevadaJobOnePopulationEquality, nevadaJobOneRounds, nevadaJobOneCoolingPeriod, 1));
		nevadaJobSummaries.add(new JobSummary(state, nevadaJobTwoNumDistrictings, nevadaJobTwoPopulationEquality, nevadaJobTwoRounds, nevadaJobTwoCoolingPeriod, 1));
		
		return nevadaJobSummaries;
	}
	
//	public List<JobSummary> getJobSummaries(USState state) {
//		List<JobSummary> jobSummaries = new ArrayList<JobSummary>();
//		HashMap<USState, String> stateToStateStringMap = new HashMap<USState, String>();
//		stateToStateStringMap.put(USState.NV, "NV");
//		stateToStateStringMap.put(USState.SC, "SC");
//		stateToStateStringMap.put(USState.WA, "WA");
//		EntityManager em = this.factory.createEntityManager();
//        em.getTransaction().begin();
//        
//		TypedQuery<DatabaseState> fetchStatesQuery = em.createQuery("SELECT s FROM DatabaseState s WHERE s.state=:stateName", DatabaseState.class);
//		fetchStatesQuery.setParameter("stateName", stateToStateStringMap.get(state));
//		List<DatabaseState> fetchedStates = fetchStatesQuery.getResultList();
//		int stateId = fetchedStates.get(0).getId();
//		
//		TypedQuery<DatabaseJob> fetchJobsQuery = em.createQuery("SELECT j FROM DatabaseJob j WHERE j.stateId=:id", DatabaseJob.class);
//		fetchStatesQuery.setParameter("id", stateId);
//		List<DatabaseJob> fetchedJobs = fetchJobsQuery.getResultList();
//		System.out.println(fetchedJobs.get(0).getJobFilePath());
//		
//		for (int i = 0; i < fetchedJobs.size(); i++) {
//			DatabaseJob currJob = fetchedJobs.get(i);
//			String currJobPath = currJob.getJobFilePath();
//			String[] currJobStringParameters = currJobPath.split("_");
//			String[] currJobPopulationEqualityString = (currJobStringParameters[3].substring(1, currJobStringParameters[3].length())).split("\\.");
//			float currJobPopulationEquality = Float.parseFloat(currJobPopulationEqualityString[0]);
//			int currJobRounds = Integer.parseInt(currJobStringParameters[2].substring(1, currJobStringParameters[2].length()));
//			int currJobCoolingPeriod = Integer.parseInt(currJobStringParameters[1].substring(1, currJobStringParameters[1].length()));
//			int currJobNumDistrictings = Integer.parseInt(currJobStringParameters[0].substring(1, currJobStringParameters[0].length()));
//			JobSummary newJobSummary = new JobSummary(state, currJobNumDistrictings, currJobPopulationEquality, currJobRounds, currJobCoolingPeriod, 1);
//			jobSummaries.add(newJobSummary);
//		}
		
//		
//		String[] nevadaJobTwoStringParameters = nevadaJobTwoFileName.split("_");
//		String[] nevadaJobTwoPopulationEqualityString = (nevadaJobTwoStringParameters[4].substring(1, nevadaJobTwoStringParameters[4].length())).split("\\.");
//		float nevadaJobTwoPopulationEquality = Float.parseFloat(nevadaJobTwoPopulationEqualityString[0]);
//		int nevadaJobTwoRounds = Integer.parseInt(nevadaJobTwoStringParameters[3].substring(1, nevadaJobTwoStringParameters[3].length()));
//		int nevadaJobTwoCoolingPeriod = Integer.parseInt(nevadaJobTwoStringParameters[2].substring(1, nevadaJobTwoStringParameters[2].length()));
//		int nevadaJobTwoNumDistrictings = Integer.parseInt(nevadaJobTwoStringParameters[1].substring(1, nevadaJobTwoStringParameters[1].length()));
//		
//		nevadaJobSummaries.add(new JobSummary(state, nevadaJobOneNumDistrictings, nevadaJobOnePopulationEquality, nevadaJobOneRounds, nevadaJobOneCoolingPeriod, nevadaJobOneFileName));
//		nevadaJobSummaries.add(new JobSummary(state, nevadaJobTwoNumDistrictings, nevadaJobTwoPopulationEquality, nevadaJobTwoRounds, nevadaJobTwoCoolingPeriod, nevadaJobTwoFileName));
//		em.getTransaction().commit();
//
//        em.close();
//		return jobSummaries;
//	}
	
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
	
//	public List<String> instantiateStatesandJobs() {
//		EntityManager em = this.factory.createEntityManager();
//        em.getTransaction().begin();
//        
//        DatabaseState nevada = new DatabaseState("NV");
//        DatabaseState southCarolina = new DatabaseState("SC");
//        DatabaseState washington = new DatabaseState("WA");
//        List<String> entitiesToStringList = new ArrayList<String>();
//        entitiesToStringList.add(nevada.toString());
//        entitiesToStringList.add(southCarolina.toString());
//        entitiesToStringList.add(washington.toString());
//		em.persist(nevada);
//		em.persist(southCarolina);
//		em.persist(washington);
//        
//        File nevadaDirectoryPath = new File("C:\\Users\\Brian\\OneDrive\\Documents\\GitHub\\tothemoon_server\\ToTheMoon\\src\\main\\java\\DistrictingData\\Nevada");
//        File nevadaJobsList[] = nevadaDirectoryPath.listFiles();
//        for(File file : nevadaJobsList) {
//        	DatabaseJob nevadaJob = new DatabaseJob();
//            String nevadaFileName = file.getName();
//            nevadaJob.setJobState(nevada);
//            nevadaJob.setJobFilePath(nevadaFileName);
//            nevadaJob.setJobNumber(1);
//            entitiesToStringList.add(nevadaJob.toString());
//    		em.persist(nevadaJob);
//        }
//        
//        File southCarolinaDirectoryPath = new File("C:\\Users\\Brian\\OneDrive\\Documents\\GitHub\\tothemoon_server\\ToTheMoon\\src\\main\\java\\DistrictingData\\SouthCarolina");
//        File southCarolinaJobsList[] = southCarolinaDirectoryPath.listFiles();
//        for(File file : southCarolinaJobsList) {
//            DatabaseJob southCarolinaJob = new DatabaseJob();
//            String southCarolinaFileName = file.getName();
//            southCarolinaJob.setJobState(southCarolina);
//            southCarolinaJob.setJobFilePath(southCarolinaFileName);
//            southCarolinaJob.setJobNumber(1);
//            entitiesToStringList.add(southCarolinaJob.toString());
//    		em.persist(southCarolinaJob);
//        }
//        
//        File washingtonDirectoryPath = new File("C:\\Users\\Brian\\OneDrive\\Documents\\GitHub\\tothemoon_server\\ToTheMoon\\src\\main\\java\\DistrictingData\\Washington");
//        File washingtonJobsList[] = washingtonDirectoryPath.listFiles();
//        for(File file : washingtonJobsList) {
//            DatabaseJob washingtonJob = new DatabaseJob();
//            String washingtonFileName = file.getName();
//            washingtonJob.setJobState(washington);
//            washingtonJob.setJobFilePath(washingtonFileName);
//            washingtonJob.setJobNumber(1);
//            entitiesToStringList.add(washingtonJob.toString());
//    		em.persist(washingtonJob);
//        }
//		
//		em.getTransaction().commit();
//
//        em.close();
//        
//        return entitiesToStringList;
//	}
//	
//	public String instantiateEnacted(USState state) {
//		EntityManager em = this.factory.createEntityManager();
//        em.getTransaction().begin();
//        
//        HashMap<USState, String> stateToStateStringMap = new HashMap<USState, String>();
//		stateToStateStringMap.put(USState.NV, "NV");
//		stateToStateStringMap.put(USState.SC, "SC");
//		stateToStateStringMap.put(USState.WA, "WA");
////        DatabaseState nevada = new DatabaseState("NV");
////        DatabaseState southCarolina = new DatabaseState("SC");
////        DatabaseState washington = new DatabaseState("WA");
//        DatabaseEnacted newEnacted = new DatabaseEnacted();
//        String enactedToString = "enactedfilepath";
//        
//        newEnacted.setEnactedFilePath(enactedToString);
//        
//        enactedToString = newEnacted.toString();
//        
//		em.persist(newEnacted);
//		
//		em.getTransaction().commit();
//
//        em.close();
//        
//        return enactedToString;
//	}
	
	public JobSummary getInitialJobSummary(USState state, int jobNumber) {
//		List<JobSummary> jobSummaries = new ArrayList<JobSummary>();
		String jobFilePath = Constants.YOUR_DIRECTORY_PREFIX + "\\" + state.name() + "\\jobs\\job" + Integer.toString(jobNumber);
		File jobDirectoryPath = new File(jobFilePath);
		File jobFileList[] = jobDirectoryPath.listFiles();
		String jobFileName = jobFileList[0].getName();
    	String[] currJobStringParameters = jobFileName.split("_");
		String[] currJobPopulationEqualityString = (currJobStringParameters[3].substring(1, currJobStringParameters[3].length())).split("\\.");
		float currJobPopulationEquality = Float.parseFloat(currJobPopulationEqualityString[0]);
		int currJobRounds = Integer.parseInt(currJobStringParameters[2].substring(1, currJobStringParameters[2].length()));
		int currJobCoolingPeriod = Integer.parseInt(currJobStringParameters[1].substring(1, currJobStringParameters[1].length()));
		int currJobNumDistrictings = (jobNumber == 1) ? 100000 : 10000;
		JobSummary newJobSummary = new JobSummary(state, currJobNumDistrictings, currJobPopulationEquality, currJobRounds, currJobCoolingPeriod, jobNumber);
//        for(File file : jobFileList) {
//        	String jobFileName = file.getName();
//        	String[] currJobStringParameters = jobFileName.split("_");
//			String[] currJobPopulationEqualityString = (currJobStringParameters[3].substring(1, currJobStringParameters[3].length())).split("\\.");
//			float currJobPopulationEquality = Float.parseFloat(currJobPopulationEqualityString[0]);
//			int currJobRounds = Integer.parseInt(currJobStringParameters[2].substring(1, currJobStringParameters[2].length()));
//			int currJobCoolingPeriod = Integer.parseInt(currJobStringParameters[1].substring(1, currJobStringParameters[1].length()));
//			int currJobNumDistrictings = (jobNumber == 1) ? 10000 : 100000;
//			JobSummary newJobSummary = new JobSummary(state, currJobNumDistrictings, currJobPopulationEquality, currJobRounds, currJobCoolingPeriod, jobFileName);
//			jobSummaries.add(newJobSummary);
//        }
        
        return newJobSummary;
	}
	
	public List<String> getJobNumberJobPaths(USState state, int jobNumber) {
		List<String> jobPaths = new ArrayList<String>();
		String jobFilePath = Constants.YOUR_DIRECTORY_PREFIX + "\\" + state.name() + "\\jobs\\job" + Integer.toString(jobNumber);
		File jobDirectoryPath = new File(jobFilePath);
		File jobFileList[] = jobDirectoryPath.listFiles();
		for(File file : jobFileList) {
			String jobFileName = file.getName();
			jobPaths.add(state.name() + "\\jobs\\job" + Integer.toString(jobNumber) + "\\" + jobFileName);
        }
		return jobPaths;
	}
	
	public String getEnactedGeometry(USState state) {
		String jobFilePath = Constants.YOUR_DIRECTORY_PREFIX + "\\" + state.name() + "\\enactedGeometry";
		File jobDirectoryPath = new File(jobFilePath);
		File jobFileList[] = jobDirectoryPath.listFiles();
		String jobFileName = jobFileList[0].getName(); 
		return state.name() + "\\enactedGeometry" + "\\" + jobFileName;
	}
	
	public String getPrecinctGeometry(USState state) {
		String jobFilePath = Constants.YOUR_DIRECTORY_PREFIX + "\\" + state.name() + "\\precinctGeometry";
		File jobDirectoryPath = new File(jobFilePath);
		File jobFileList[] = jobDirectoryPath.listFiles();
		String jobFileName = jobFileList[0].getName(); 
        return state.name() + "\\precinctGeometry" + "\\" + jobFileName;
	}
	
	public String getJTSGeometry(USState state) {
		String jobFilePath = Constants.YOUR_DIRECTORY_PREFIX + "\\" + state.name() + "\\jtsGeometry";
		File jobDirectoryPath = new File(jobFilePath);
		File jobFileList[] = jobDirectoryPath.listFiles();
		String jobFileName = jobFileList[0].getName(); 
        return state.name() + "\\jtsGeometry" + "\\" + jobFileName;
	}
}
