package com.restapi.ToTheMoon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class State {

	private USState currState;
	private Job currJob;
	private List<JobSummary> jobSummaries;
	private Job currConstrainedJob;
	// private Districting currDistricting;
	private Districting enactedDistricting;
	private Map<Constraints, Float> currConstraints;
	private MinorityPopulation currMinorityPopulation;
	
	public State() {
		
	}
	
	public State(USState state, Districting enactedDistricting, List<JobSummary> jobSummaries) {
		currState = state;
		this.enactedDistricting = enactedDistricting;
		this.jobSummaries = jobSummaries;
	}

	public USState getCurrState() {
		return currState;
	}

	public void setCurrState(USState currState) {
		this.currState = currState;
	}

	public Job getCurrJob() {
		return currJob;
	}

	public void setCurrJob(Job currJob) {
		this.currJob = currJob;
	}

	public List<JobSummary> getJobSummaries() {
		return jobSummaries;
	}

	public void setJobSummaries(List<JobSummary> jobSummaries) {
		this.jobSummaries = jobSummaries;
	}

	public Job getCurrConstrainedJob() {
		return currConstrainedJob;
	}

	public void setCurrConstrainedJob(Job currConstrainedJob) {
		this.currConstrainedJob = currConstrainedJob;
	}

	public Districting getEnactedDistricting() {
		return enactedDistricting;
	}

	public void setEnactedDistricting(Districting enactedDistricting) {
		this.enactedDistricting = enactedDistricting;
	}

	public Map<Constraints, Float> getCurrConstraints() {
		return currConstraints;
	}

	public void setCurrConstraints(Map<Constraints, Float> currConstraints) {
		this.currConstraints = currConstraints;
	}

	public MinorityPopulation getCurrMinorityPopulation() {
		return currMinorityPopulation;
	}

	public void setCurrMinorityPopulation(MinorityPopulation currMinorityPopulation) {
		this.currMinorityPopulation = currMinorityPopulation;
	}
	
	// Incomplete method
	public void generateEnactedDistricting(String enactedFile, String geoFile) throws FileNotFoundException, IOException, ParseException {
		Object enactedObject = new JSONParser().parse(new FileReader(enactedFile));
		JSONObject enactedJsonObject = (JSONObject) enactedObject;
		JSONArray districtsArray = (JSONArray) enactedJsonObject.get("features");
		
		ArrayList<District> districtList = new ArrayList<District>();
		ArrayList<ArrayList<Precinct>> listOfPrecinctLists = new ArrayList<ArrayList<Precinct>>();
		ArrayList<Integer> hCVAPList = new ArrayList<Integer>();
		ArrayList<Integer> bCVAPList = new ArrayList<Integer>();
		ArrayList<Integer> asianCVAPList = new ArrayList<Integer>();
		ArrayList<Map<MinorityPopulation, Integer>> listOfMinorityPopulationMaps = new ArrayList<Map<MinorityPopulation, Integer>>();
		ArrayList<Map<MinorityPopulation, Float>> listOfMinorityPercentageMaps = new ArrayList<Map<MinorityPopulation, Float>>();
		
		for (int i = 0 ; i < districtsArray.size(); i++) {
			JSONObject currDistrict = (JSONObject) districtsArray.get(i);
			JSONObject props = (JSONObject) currDistrict.get("properties");
			Double area = (Double) props.get("area");
			District d = new District();
			d.setArea(area.floatValue());
			districtList.add(d);
			listOfPrecinctLists.add(new ArrayList<Precinct>());
			hCVAPList.add(0);
			bCVAPList.add(0);
			asianCVAPList.add(0);
			listOfMinorityPopulationMaps.add(new HashMap<MinorityPopulation, Integer>());
			listOfMinorityPercentageMaps.add(new HashMap<MinorityPopulation, Float>());
		}
		
		Object geoObject = new JSONParser().parse(new FileReader(geoFile));
		JSONObject geoJsonObject = (JSONObject) geoObject;
		JSONArray allPrecinctsArray = (JSONArray) geoJsonObject.get("features");
		for (int i = 0 ; i < allPrecinctsArray.size(); i++) {
	    	JSONObject precinctObject = (JSONObject) allPrecinctsArray.get(i);
	    	JSONObject propertiesObject = (JSONObject) precinctObject.get("properties");
	    	
	    	int districtNumber = ((Long)propertiesObject.get("DISTRICT")).intValue();
	    	Double hCVAP = (Double) propertiesObject.get("HCVAP");
    		Double wCVAP = (Double) propertiesObject.get("WCVAP");
    		Double bCVAP = (Double) propertiesObject.get("BCVAP");
    		Double asianCVAP = (Double) propertiesObject.get("ASIANCVAP");
    		Double totalCVAP = (Double) propertiesObject.get("CVAP");
    		Double totalPopulation = (Double) propertiesObject.get("TOTPOP");
    		
    		hCVAPList.set(districtNumber, hCVAPList.get(districtNumber) + hCVAP.intValue());
    		bCVAPList.set(districtNumber, bCVAPList.get(districtNumber) + bCVAP.intValue());
    		asianCVAPList.set(districtNumber, asianCVAPList.get(districtNumber) + asianCVAP.intValue());
    		
    		Precinct newPrecinct = new Precinct();
    		newPrecinct.setId(i);
    		newPrecinct.setHispanicPopulation(hCVAP.intValue());
    		newPrecinct.setBlackPopulation(bCVAP.intValue());
    		newPrecinct.setAsianPopulation(asianCVAP.intValue());
    		newPrecinct.setVotingPopulation(totalCVAP.intValue());
    		newPrecinct.setPopulation(totalPopulation.intValue());
    		
    		District correspondingDistrict = districtList.get(districtNumber);
    		correspondingDistrict.setPopulation(correspondingDistrict.getPopulation() + totalPopulation.intValue());
    		correspondingDistrict.setVotingPopulation(correspondingDistrict.getVotingPopulation() + totalCVAP.intValue());   		
    		ArrayList<Precinct> correspondingPrecinctList = listOfPrecinctLists.get(districtNumber);
    		correspondingPrecinctList.add(newPrecinct);
		}
		
		for (int i = 0 ; i < districtList.size(); i++) {
			District currentDistrict = districtList.get(i);

			Map<MinorityPopulation, Integer> currentMinorityPopulationMap = listOfMinorityPopulationMaps.get(i);
			currentMinorityPopulationMap.put(MinorityPopulation.HISPANIC, hCVAPList.get(i));
			currentMinorityPopulationMap.put(MinorityPopulation.AFRICAN_AMERICAN, bCVAPList.get(i));
			currentMinorityPopulationMap.put(MinorityPopulation.ASIAN, asianCVAPList.get(i));
			
			Map<MinorityPopulation, Float> currentMinorityPercentageMap = listOfMinorityPercentageMaps.get(i);
			float hCVAPPercentage = hCVAPList.get(i).floatValue() / currentDistrict.getVotingPopulation();
			float bCVAPPercentage = bCVAPList.get(i).floatValue() / currentDistrict.getVotingPopulation();
			float asianCVAPPercentage = asianCVAPList.get(i).floatValue() / currentDistrict.getVotingPopulation();
			currentMinorityPercentageMap.put(MinorityPopulation.HISPANIC, hCVAPPercentage);
			currentMinorityPercentageMap.put(MinorityPopulation.AFRICAN_AMERICAN, bCVAPPercentage);
			currentMinorityPercentageMap.put(MinorityPopulation.ASIAN, asianCVAPPercentage);
			
			currentDistrict.setMinorityPopulations(currentMinorityPopulationMap);
			currentDistrict.setMinorityPopulationPercentages(currentMinorityPercentageMap);
			currentDistrict.setPrecincts(listOfPrecinctLists.get(i));
			currentDistrict.setLabel(i);
		}
		Districting enactedDistricting = new Districting();
		enactedDistricting.setDistricts(districtList);
		this.setEnactedDistricting(enactedDistricting);
	}

}
