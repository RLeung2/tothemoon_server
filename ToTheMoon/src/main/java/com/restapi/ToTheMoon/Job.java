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
	
	private List<Districting> districtings;
	private float populationEquality;
	private MinorityPopulation currMinorityPopulation;
	private DistrictingAnalysisSummary districtingAnalysisSummary;
	private String id;
	private BoxAndWhisker boxAndWhisker;
	private Map<Integer, Integer> precinctPopulationMap;
	private Districting enactedDistricting;
	
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
	    
	    List<District> enactedDistrictsList = this.enactedDistricting.getDistricts();
	    int numDistricts = enactedDistrictsList.size();
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
	    	
	    	Map<Integer, Float> enactedPercentagesMap = boxAndWhisker.getEnactedDots();
	    	District enactedDistrict = enactedDistrictsList.get(i);
	    	float enactedPercentage = enactedDistrict.getMinorityPopulationPercentageForMinority(this.currMinorityPopulation);
	    	enactedPercentagesMap.put(i, enactedPercentage);
	    	boxAndWhisker.setEnactedDots(enactedPercentagesMap);
	    }
		boxAndWhisker.setMinorityPopulation(this.currMinorityPopulation);
		this.setBoxAndWhisker(boxAndWhisker);
	}
	
	private List<Float> getMinorityPercentagesListAtDistrictIndex(int index) {
		List<Float> minorityPercentagesList = new ArrayList<Float>();
		
		for (Districting districting : this.districtings) {
			District district = districting.getDistricts().get(index);
			Map<MinorityPopulation, Float> minorityPercentagesMap = district.getMinorityPopulationPercentages();
			
			minorityPercentagesList.add(minorityPercentagesMap.get(this.currMinorityPopulation));
		}
		
		return minorityPercentagesList;
	}
	
	private static float percentile(List<Float> elements, double percentile) {
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
		for (Districting districting : this.districtings) {
	    	districting.gillConstructRenumbering(this.enactedDistricting);
	    }
	}

	public Collection<Districting> getDistrictings() {
		return districtings;
	}

	public void setDistrictings(List<Districting> districtings) {
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

	public Map<Integer, Integer> getPrecinctPopulationMap() {
		return precinctPopulationMap;
	}

	public void setPrecinctPopulationMap(Map<Integer, Integer> precinctPopulationMap) {
		this.precinctPopulationMap = precinctPopulationMap;
	}

	public Districting getEnactedDistricting() {
		return enactedDistricting;
	}

	public void setEnactedDistricting(Districting enactedDistricting) {
		this.enactedDistricting = enactedDistricting;
	}

	public Districting getDistrictingAtIndex(int index) {
		return this.districtings.get(index);
	}
	
	public void generatePrecinctPopulationMap(String geoFile) throws FileNotFoundException, IOException, ParseException {
		Object geoObject = new JSONParser().parse(new FileReader(geoFile));
		JSONObject geoJsonObject = (JSONObject) geoObject;
		JSONArray allPrecinctsArray = (JSONArray) geoJsonObject.get("features");
		Map<Integer, Integer> precinctPopulationMap = new HashMap<Integer, Integer>();
		for (int i = 0 ; i < allPrecinctsArray.size(); i++) {
	    	JSONObject precinctObject = (JSONObject) allPrecinctsArray.get(i);
	    	JSONObject propertiesObject = (JSONObject) precinctObject.get("properties");
	    	
	    	int precinctId = Integer.parseInt((String)propertiesObject.get("id"));
    		Double totalPopulation = (Double) propertiesObject.get("TOTPOP");
    		precinctPopulationMap.put(precinctId, totalPopulation.intValue());
		}
		this.setPrecinctPopulationMap(precinctPopulationMap);
	}
	
	public void fillDistrictings() throws FileNotFoundException, IOException, ParseException {
		ArrayList<Districting> districtingList = new ArrayList<Districting>();
		Object obj = new JSONParser().parse(new FileReader("D:\\Users\\Documents\\GitHub\\tothemoon_server\\ToTheMoon\\src\\main\\java\\DistrictingData\\nv_d1000_c1000_r25_p10.json"));
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
	    		
	    		List<Long> precinctIDs = (List<Long>) districtObject.get("precincts");
	    		
	    		Double totalVAP = hVAP + wVAP + bVAP + asianVAP;
	    		float hVAPPercentage = hVAP.floatValue() / totalVAP.floatValue();
	    		float bVAPPercentage = bVAP.floatValue() / totalVAP.floatValue();
	    		float asianVAPPercentage = asianVAP.floatValue() / totalVAP.floatValue();

	    		//Long districtNumber = (Long) districtObject.get("districtNumber");
	    		Double hCVAP = (Double) districtObject.get("HCVAP");
	    		Double wCVAP = (Double) districtObject.get("WCVAP");
	    		Double bCVAP = (Double) districtObject.get("BCVAP");
	    		Double asianCVAP = (Double) districtObject.get("ASIANCVAP");
	    		
	    		Double totalVAP = hCVAP + wCVAP + bCVAP + asianCVAP;
	    		float hVAPPercentage = hCVAP.floatValue() / totalVAP.floatValue();
	    		float bVAPPercentage = bCVAP.floatValue() / totalVAP.floatValue();
	    		float asianVAPPercentage = asianCVAP.floatValue() / totalVAP.floatValue();
	    		
	    		HashMap<MinorityPopulation, Float> minorityPercentagesMap = new HashMap<MinorityPopulation, Float>();
	    		minorityPercentagesMap.put(MinorityPopulation.HISPANIC, hVAPPercentage);
	    		minorityPercentagesMap.put(MinorityPopulation.AFRICAN_AMERICAN, bVAPPercentage);
	    		minorityPercentagesMap.put(MinorityPopulation.ASIAN, asianVAPPercentage);
	    		
	    		JSONArray precinctsArray = (JSONArray) districtObject.get("precincts");
	    		ArrayList<Precinct> precinctList = new ArrayList<Precinct>();
	    		for (int k = 0; k < precinctsArray.size(); k++) {
	    			Precinct newPrecinct = new Precinct(0, 0, 0, 0, 0, 0, 0, null, null, 0);
	    			int precinctId = ((Long) precinctsArray.get(k)).intValue() - 1;
	    			newPrecinct.setId(precinctId);
	    			newPrecinct.setPopulation(this.precinctPopulationMap.get(precinctId));
	    			precinctList.add(newPrecinct);
	    		}
	    		
	    		District district = new District();
	    		district.setMinorityPopulationPercentages(minorityPercentagesMap);
	    		district.setPrecinctIDs(precinctIDs);
	    		district.setPrecincts(precinctList);
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
