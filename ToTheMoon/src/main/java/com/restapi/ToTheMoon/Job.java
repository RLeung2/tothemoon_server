package com.restapi.ToTheMoon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Job {
	
	private Collection<Districting> districtings;
	private float populationEquality;
	private MinorityPopulation currMinorityPopulation;
	private DistrictingAnalysisSummary districtingAnalysisSummary;
	private String id;
	private BoxAndWhisker boxAndWhisker;
	
	public Job() {
		this.boxAndWhisker = new BoxAndWhisker();
	}

//	TODO
	public Job constrain(Map<Constraints, Float> filtersMap) {
		return null;
	}
	
	public void generateBoxAndWhiskerData() {
		BoxAndWhisker boxAndWhisker = this.getBoxAndWhisker();
	    
	    for (Districting districting : this.districtings) {
	    	districting.sortDistrictsByMinority(this.currMinorityPopulation);
	    }
	    
	    Districting randomDistricting = this.districtings.iterator().next();
	    int numDistricts = randomDistricting.getDistricts().size();
	    for (int i = 0; i < numDistricts; i++) {
	    	List<Float> minorityPercentagesList = getMinorityPercentagesListAtDistrictIndex(i);
	    	Collections.sort(minorityPercentagesList);
	    	
	    	Map<Integer, Float> minPopulationMap = boxAndWhisker.getMinPopDistrict();
	    	minPopulationMap.put(i, minorityPercentagesList.get(0));
	    	boxAndWhisker.setMinPopDistrict(minPopulationMap);
	    	
	    	Map<Integer, Float> maxPopulationMap = boxAndWhisker.getMaxPopDistrict();
	    	maxPopulationMap.put(i, percentile(minorityPercentagesList, 100));
	    	boxAndWhisker.setMaxPopDistrict(maxPopulationMap);
	    	
	    	Map<Integer, Float> twentyFifthPercentileMap = boxAndWhisker.getTwentyFifthPercentile();
	    	twentyFifthPercentileMap.put(i, percentile(minorityPercentagesList, 25));
	    	boxAndWhisker.setTwentyFifthPercentile(twentyFifthPercentileMap);
	    	
	    	Map<Integer, Float> averagePopulationMap = boxAndWhisker.getAveragePopDistrict();
	    	averagePopulationMap.put(i, percentile(minorityPercentagesList, 50));
	    	boxAndWhisker.setAveragePopDistrict(averagePopulationMap);
	    	
	    	Map<Integer, Float> seventyFifthPercentileMap = boxAndWhisker.getSeventyFifthPercentile();
	    	seventyFifthPercentileMap.put(i, percentile(minorityPercentagesList, 75));
	    	boxAndWhisker.setSeventyFifthPercentile(seventyFifthPercentileMap);
	    }
		boxAndWhisker.setMinorityPopulation(this.currMinorityPopulation);
		this.setBoxAndWhisker(boxAndWhisker);
	}
	
	public List<Float> getMinorityPercentagesListAtDistrictIndex(int index) {
		List<Float> minorityPercentagesList = new ArrayList<Float>();
		
		for (Districting districting : this.districtings) {
			District district = districting.getDistricts().get(index);
			Map<MinorityPopulation, Float> minorityPercentagesMap = district.getMinorityPopulationPercentages();
			
			minorityPercentagesList.add(minorityPercentagesMap.get(this.currMinorityPopulation));
		}
		
		return minorityPercentagesList;
	}
	
	public static float percentile(List<Float> elements, double percentile) {
	    Collections.sort(elements);
	    int index = (int) Math.ceil(percentile / 100.0 * elements.size());
	    return elements.get(index-1);
	}
	
//	TODO
	public void instantiateObjectiveScores(Map<Measures, Float> measuresMap, Map<Constraints, Float> constraintsMap) {
		
	}
	
//	TODO
	public void generateDistrictingAnalysisSummary(DistrictingAnalysisSummary districtingAnalysisSummary) {
		
	}
	
//	TODO
	public void renumberDistrictings() {
		
	}

	public Collection<Districting> getDistrictings() {
		return districtings;
	}

	public void setDistrictings(Collection<Districting> districtings) {
		this.districtings = districtings;
	}

	public float getPopulationEquality() {
		return populationEquality;
	}

	public void setPopulationEquality(float populationEquality) {
		this.populationEquality = populationEquality;
	}

	public MinorityPopulation getCurrMinorityPopulation() {
		return currMinorityPopulation;
	}

	public void setCurrMinorityPopulation(MinorityPopulation currMinorityPopulation) {
		this.currMinorityPopulation = currMinorityPopulation;
	}

	public DistrictingAnalysisSummary getDistrictingAnalysisSummary() {
		return districtingAnalysisSummary;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BoxAndWhisker getBoxAndWhisker() {
		return boxAndWhisker;
	}
	
	public BoxAndWhisker getBoxAndWhisker(int districtingId) {
		return boxAndWhisker;
	}
	
	public void setBoxAndWhisker(BoxAndWhisker boxAndWhisker) {
		this.boxAndWhisker = boxAndWhisker;
	}
	
	@SuppressWarnings("unchecked")
	public void fillDistrictings() throws FileNotFoundException, IOException, ParseException {
		ArrayList<Districting> districtingList = new ArrayList<Districting>();
		Object obj = new JSONParser().parse(new FileReader("C:\\Users\\Ahmed\\git\\tothemoon\\ToTheMoon\\src\\main\\java\\DistrictingData\\nv_d1000_c1000_r25_p10.json"));
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray plansArray = (JSONArray) jsonObject.get("plans");

	    for (int i = 0 ; i < plansArray.size(); i++) {
	    	JSONObject planObject = (JSONObject) plansArray.get(i);
	    	JSONArray districtsArray = (JSONArray) planObject.get("districts");
	    	Districting newDistricting = new Districting();
	    	ArrayList<District> districtList = new ArrayList<District>();
	    	
	    	for (int j = 0; j < districtsArray.size(); j++) {
	    		JSONObject districtObject = (JSONObject) districtsArray.get(j);
	    		Double hVAP = (Double) districtObject.get("HCVAP");
	    		Double wVAP = (Double) districtObject.get("WCVAP");
	    		Double bVAP = (Double) districtObject.get("BCVAP");
	    		Double asianVAP = (Double) districtObject.get("ASIANCVAP");
	    		
	    		List<Integer> precinctIDs = (List<Integer>) districtObject.get("precincts");
	    		
	    		Double totalVAP = hVAP + wVAP + bVAP + asianVAP;
	    		float hVAPPercentage = hVAP.floatValue() / totalVAP.floatValue();
	    		float bVAPPercentage = bVAP.floatValue() / totalVAP.floatValue();
	    		float asianVAPPercentage = asianVAP.floatValue() / totalVAP.floatValue();
	    		
	    		HashMap<MinorityPopulation, Float> minorityPercentagesMap = new HashMap<MinorityPopulation, Float>();
	    		minorityPercentagesMap.put(MinorityPopulation.HISPANIC, hVAPPercentage);
	    		minorityPercentagesMap.put(MinorityPopulation.AFRICAN_AMERICAN, bVAPPercentage);
	    		minorityPercentagesMap.put(MinorityPopulation.ASIAN, asianVAPPercentage);
	    		
	    		District district = new District();
	    		district.setMinorityPopulationPercentages(minorityPercentagesMap);
	    		district.setPrecinctIDs(precinctIDs);
	    		districtList.add(district);
	    	}
	    	newDistricting.setDistricts(districtList);
	    	districtingList.add(newDistricting);
	    }
	    this.setDistrictings(districtingList);
	}
	
	public String generateDistrictingGeometry(int index) throws JsonParseException, JsonMappingException, IOException {
		List<Districting> districtings = (ArrayList<Districting>) this.districtings;
		Districting selectedDistricting = (Districting) (districtings).get(index);
		String geometryFileName = "C:\\Users\\Ahmed\\git\\tothemoon\\ToTheMoon\\src\\main\\java\\DistrictingData\\nv_simple.json";
		
		return selectedDistricting.generateDistrictingGeoJSON(geometryFileName, selectedDistricting);
	}
}
