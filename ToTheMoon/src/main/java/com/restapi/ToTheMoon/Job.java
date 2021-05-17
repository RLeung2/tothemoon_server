package com.restapi.ToTheMoon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Job {
	
	private List<Districting> districtings;
	private float populationEquality;
	private MinorityPopulation currMinorityPopulation;
	private DistrictingAnalysisSummary districtingAnalysisSummary;
	private String id;
	private BoxAndWhisker boxAndWhisker;
	private Map<Integer, Integer> precinctPopulationMap;
	private Map<Integer, Float> precinctAreaMap;
	private Districting enactedDistricting;
	private Districting averageDistricting;
	
	public Job() {
		this.boxAndWhisker = new BoxAndWhisker();
	}

//	TODO
	public Job constrain(Map<Constraints, Float> filtersMap) {
		return null;
	}
	
	public Districting constrain(double popEq, String popScoreType, String popType, double compactness, double minorityThreshhold, int minNumMajMinDistrict, String minorityType, List<Integer> incumbents, JsonObject plan, int index) {
		// Compactness
		if (plan.get("gr_compact").getAsDouble() < compactness) {
			return null;
		}
		
		// Population Equality -- adjust for different population types
		if (plan.get(popScoreType).getAsDouble() < popEq) {
			return null;
		}
		
		// Majority Minority
		int currMinMajDistricts = 0;
    	JsonArray districtsArray = plan.get("districts").getAsJsonArray();
    	for (int j = 0; j < districtsArray.size(); j++) {
    		JsonObject districtObject = districtsArray.get(j).getAsJsonObject();
	    	if(districtObject.get(minorityType).getAsDouble() >= minorityThreshhold)
	    		currMinMajDistricts += 1;
    	}
    	
    	if (currMinMajDistricts < minNumMajMinDistrict) 
    		return null;
    	
		// Incumbent protection
		ArrayList<Integer> incumbentsProtectedInPlan = new ArrayList<Integer>();     
		JsonArray jArray = plan.get("incumbentsProtected").getAsJsonArray();
		if (jArray != null) { 
		   for (int i = 0; i < jArray.size(); i++){ 
			   incumbentsProtectedInPlan.add(jArray.get(i).getAsInt());
		   } 
		} 
		
		if (!incumbentsProtectedInPlan.containsAll(incumbents)) {
			return null;
		}

    	// Fill districting logic of creating DistrictingObject goes here
    	Districting d = readJsonToDistricting(plan, popType, index);
    	d.setObjectivefunction(new ObjectiveFunction());
    	d.getObjectivefunction().setCompactnessScore(plan.get("gr_compact").getAsFloat());
    	d.setMajorityMinorityDistricts(currMinMajDistricts);
		return d;
	}
	
	public void generateBoxAndWhiskerData() {
		BoxAndWhisker boxAndWhisker = this.getBoxAndWhisker();
	    
	    for (Districting districting : this.districtings) {
	    	districting.sortDistrictsByMinority(this.currMinorityPopulation);
	    }
	    
	    this.enactedDistricting.sortDistrictsByMinority(currMinorityPopulation);
	    List<District> enactedDistrictsList = this.enactedDistricting.getDistricts();
	    int numDistricts = enactedDistrictsList.size();
	    for (int i = 0; i < numDistricts; i++) {
	    	List<Float> minorityPercentagesList = getMinorityPercentagesListAtDistrictIndex(i);
	    	Collections.sort(minorityPercentagesList);
	    	
	    	Map<Integer, Float> minPopulationMap = boxAndWhisker.getMinPopDistrict();
	    	minPopulationMap.put(i, percentile(minorityPercentagesList, 0));
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
	
	public List<Float> getMinorityPercentagesListAtDistrictIndex(int index) {
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
	    if (percentile == 0)
	    	return elements.get(0);
	    int index = (int) Math.ceil(percentile / 100.0 * elements.size());
	    return elements.get(index-1);
	}
	
	
//	TODO
	public void renumberDistrictings() {
		List<Districting> topTenByObjectiveScore = this.districtingAnalysisSummary.getTopTenObjectiveScores();
		List<Districting> highScoringPlansCloseToEnacted = this.districtingAnalysisSummary.getPlansCloseToEnacted();
		List<Districting> highScoringMajorityMinorityPlans = this.districtingAnalysisSummary.getHighScoringMajMinDistricts();
		
		for (Districting districting : topTenByObjectiveScore) {
	    	districting.gillConstructRenumbering(this.enactedDistricting);
	    }
		for (Districting districting : highScoringPlansCloseToEnacted) {
	    	districting.gillConstructRenumbering(this.enactedDistricting);
	    }
		for (Districting districting : highScoringMajorityMinorityPlans) {
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
	
	public Districting getAverageDistricting() {
		return averageDistricting;
	}

	public void setAverageDistricting(Districting averageDistricting) {
		this.averageDistricting = averageDistricting;
	}
	
	public void applyMeasures(float popEqWeight, float compactnessWeight, float devFromAvgWeight, 
			float devFromEnactedPopWeight, float devFromEnactedAreaWeight) {
		for (int i = 0; i < this.districtings.size(); i++) {
			Districting currDistricting = this.districtings.get(i);
			currDistricting.calculatePopEqualityScore();
			currDistricting.calculateDevFromEnacted(this.enactedDistricting, this.currMinorityPopulation);
			currDistricting.calculateDevFromAvg(this.averageDistricting, this.currMinorityPopulation);
			currDistricting.calculateEnactedAreaScore(this.enactedDistricting);
			
			currDistricting.calculateObjScore(popEqWeight, compactnessWeight, devFromAvgWeight, devFromEnactedPopWeight, devFromEnactedAreaWeight);
		}
		
	}

	public void generatePrecinctPopulationMap(String geoFile) throws FileNotFoundException, IOException, ParseException {
		Object geoObject = new JSONParser().parse(new FileReader(geoFile));
		JSONObject geoJsonObject = (JSONObject) geoObject;
		JSONArray allPrecinctsArray = (JSONArray) geoJsonObject.get("features");
		Map<Integer, Integer> precinctPopulationMap = new HashMap<Integer, Integer>();
		
		Map<Integer, Float> precinctAreaMap = new HashMap<Integer, Float>();

		for (int i = 0 ; i < allPrecinctsArray.size(); i++) {
	    	JSONObject precinctObject = (JSONObject) allPrecinctsArray.get(i);
	    	JSONObject propertiesObject = (JSONObject) precinctObject.get("properties");
	    	
	    	int precinctId = ((Long)propertiesObject.get("id")).intValue();
    		Double totalPopulation = (Double) propertiesObject.get("TOTPOP");
    		if (totalPopulation == null) {
    			totalPopulation = 0.0;
    		}
    		precinctPopulationMap.put(precinctId, totalPopulation.intValue());
    		
    		double area = (Double) propertiesObject.get("area");
    		precinctAreaMap.put(precinctId, (float)area);

		}
		this.setPrecinctPopulationMap(precinctPopulationMap);
		this.setPrecinctAreaMap(precinctAreaMap);
	}
	
	public Districting readJsonToDistricting(JsonObject planObject, String populationType, int index) {
		JsonArray districtsArray = planObject.get("districts").getAsJsonArray();
    	Districting newDistricting = new Districting();
    	ArrayList<District> districtList = new ArrayList<District>();
    	
    	for (int j = 0; j < districtsArray.size(); j++) {
    		JsonObject districtObject = districtsArray.get(j).getAsJsonObject();

    		//Long districtNumber = (Long) districtObject.get("districtNumber");
    		HashMap<MinorityPopulation, Integer> minorityPopulationsMap = new HashMap<MinorityPopulation, Integer>();
    		HashMap<MinorityPopulation, Float> minorityPercentagesMap = new HashMap<MinorityPopulation, Float>();
    		if (populationType.equals("VAP")) {
	    		Double hCVAP = districtObject.get("H" + populationType).getAsDouble();
	    		Double wCVAP = districtObject.get("W" + populationType).getAsDouble();
	    		Double bCVAP = districtObject.get("B" + populationType).getAsDouble();
	    		Double asianCVAP = districtObject.get("ASIAN" + populationType).getAsDouble();
	    		
	    		float hVAPPercentage = districtObject.get("HTPERCENTAGE").getAsFloat();
	    		float bVAPPercentage = districtObject.get("BTPERCENTAGE").getAsFloat();
	    		float asianVAPPercentage = districtObject.get("ATPERCENTAGE").getAsFloat();
	    		
	    		minorityPopulationsMap.put(MinorityPopulation.HISPANIC, hCVAP.intValue());
	    		minorityPopulationsMap.put(MinorityPopulation.AFRICAN_AMERICAN, bCVAP.intValue());
	    		minorityPopulationsMap.put(MinorityPopulation.ASIAN, asianCVAP.intValue());
	    		
	    		minorityPercentagesMap.put(MinorityPopulation.HISPANIC, hVAPPercentage);
	    		minorityPercentagesMap.put(MinorityPopulation.AFRICAN_AMERICAN, bVAPPercentage);
	    		minorityPercentagesMap.put(MinorityPopulation.ASIAN, asianVAPPercentage);
    		}
    		else {
	    		Double hTotal = districtObject.get("HISP").getAsDouble();
	    		Double wTotal = districtObject.get("WHITE").getAsDouble();
	    		Double bTotal = districtObject.get("BLACK").getAsDouble();
	    		Double asianTotal = districtObject.get("ASIAN").getAsDouble();
	    		
	    		float hVAPPercentage = districtObject.get("HPERCENTAGE").getAsFloat();
	    		float bVAPPercentage = districtObject.get("BPERCENTAGE").getAsFloat();
	    		float asianVAPPercentage = districtObject.get("APERCENTAGE").getAsFloat();
	    		
	    		minorityPopulationsMap.put(MinorityPopulation.HISPANIC, hTotal.intValue());
	    		minorityPopulationsMap.put(MinorityPopulation.AFRICAN_AMERICAN, bTotal.intValue());
	    		minorityPopulationsMap.put(MinorityPopulation.ASIAN, asianTotal.intValue());
	    		
	    		minorityPercentagesMap.put(MinorityPopulation.HISPANIC, hVAPPercentage);
	    		minorityPercentagesMap.put(MinorityPopulation.AFRICAN_AMERICAN, bVAPPercentage);
	    		minorityPercentagesMap.put(MinorityPopulation.ASIAN, asianVAPPercentage);
    		}
    		Double totalVAP = districtObject.get("VAP").getAsDouble();
    		Double totalPopulation = districtObject.get("population").getAsDouble();
    		
    		JsonArray precinctsArray = districtObject.get("precincts").getAsJsonArray();
    		ArrayList<Precinct> precinctList = new ArrayList<Precinct>();
    		for (int k = 0; k < precinctsArray.size(); k++) {
    			Precinct newPrecinct = new Precinct(0, 0, 0, 0, 0, 0, 0, null, null, 0);
    			int precinctId = precinctsArray.get(k).getAsInt() - 1;
    			newPrecinct.setId(precinctId);
    			newPrecinct.setPopulation(this.precinctPopulationMap.get(precinctId));
    			
    			newPrecinct.setArea(this.precinctAreaMap.get(precinctId));
    			precinctList.add(newPrecinct);
    		}
    		
    		District district = new District();
    		district.setMinorityPopulations(minorityPopulationsMap);
    		district.setMinorityPopulationPercentages(minorityPercentagesMap);
    		district.setPrecincts(precinctList);
    		district.setObjectiveFunction(new ObjectiveFunction());
    		district.getObjectiveFunction().setCompactnessScore(districtObject.get("gr_compact").getAsFloat());
    		district.setPopulation(totalPopulation.intValue());
    		district.setVotingPopulation(totalVAP.intValue());
    		districtList.add(district);
    	}
    	newDistricting.setDistricts(districtList);
    	newDistricting.setId(index);
    	newDistricting.generateObjectiveFunction();
		return newDistricting;
	}
	
	public void fillDistrictings() throws FileNotFoundException, IOException, ParseException {
		ArrayList<Districting> districtingList = new ArrayList<Districting>();
		String jobName = Constants.YOUR_DIRECTORY_PREFIX + Constants.NEVADA_JOB_1000_FILE_NAME;
//		Object obj = new JSONParser().parse(new FileReader("D:\\Users\\Documents\\GitHub\\tothemoon_server\\ToTheMoon\\src\\main\\java\\DistrictingData\\nv_d1000_c1000_r25_p10.json"));
		Object obj = new JSONParser().parse(new FileReader(jobName));
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray plansArray = (JSONArray) jsonObject.get("plans");
		
	    for (int i = 0 ; i < plansArray.size(); i++) {
	    	JSONObject planObject = (JSONObject) plansArray.get(i);
	    	JSONArray districtsArray = (JSONArray) planObject.get("districts");
	    	Districting newDistricting = new Districting();
	    	ArrayList<District> districtList = new ArrayList<District>();
	    	
	    	for (int j = 0; j < districtsArray.size(); j++) {
	    		JSONObject districtObject = (JSONObject) districtsArray.get(j);
	    		
	    		List<Long> precinctIDs = (List<Long>) districtObject.get("precincts");

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
	    			
	    			newPrecinct.setArea(this.precinctAreaMap.get(precinctId));
	    			precinctList.add(newPrecinct);
	    		}
	    		
	    		District district = new District();
	    		district.setMinorityPopulationPercentages(minorityPercentagesMap);
	    		district.setPrecinctIDs(precinctIDs);
	    		district.setPrecincts(precinctList);
	    		districtList.add(district);
	    	}
	    	newDistricting.setDistricts(districtList);
	    	newDistricting.setId(i);
	    	newDistricting.generateObjectiveFunction();
	    	districtingList.add(newDistricting);
	    }
	    this.setDistrictings(districtingList);
	}
	
	public String generateDistrictingGeometry(int index, String geoFileName) throws JsonParseException, JsonMappingException, IOException {
		List<Districting> districtings = this.districtings;
		Districting selectedDistricting = districtings.get(index);
		String geometryFileName = geoFileName;
		return selectedDistricting.generateDistrictingGeoJSON(geometryFileName, selectedDistricting);
	}
	
	public void findAverageDistricting() {
		float minimumTotalMinorityPercentageDifference = Float.MAX_VALUE;
		int closestToAverageDistrictingId = 0;
		
		for (Districting districting: this.districtings) {
			List<District> districtsList = districting.getDistricts();
			float totalMinorityPercentageDifference = 0;
			
			for (int i = 0; i < districtsList.size(); i++) {
				District currentDistrict = districtsList.get(i);
				totalMinorityPercentageDifference += calculatePercentageDifferenceOfDitrictComparedToAverage(currentDistrict, i);
			}
			
			if (totalMinorityPercentageDifference < minimumTotalMinorityPercentageDifference) {
				closestToAverageDistrictingId = districting.getId();
				minimumTotalMinorityPercentageDifference = totalMinorityPercentageDifference;
			}
		}
		this.setAverageDistricting(getDistrictingById(closestToAverageDistrictingId));
	}
	
	private float calculatePercentageDifferenceOfDitrictComparedToAverage(District district, int index) {
		Map<Integer, Float> averagePopulationPercentagesMap = this.boxAndWhisker.getAveragePopDistrict();
		Map<MinorityPopulation, Float> currentMinorityPopulationPercentagesMap = district.getMinorityPopulationPercentages();
		float districtAveragePopulationPercentage = averagePopulationPercentagesMap.get(index);
		float currentDistrictPopulationPercentage = currentMinorityPopulationPercentagesMap.get(this.currMinorityPopulation);
		float currentMinorityPercentageDifference = Math.abs(districtAveragePopulationPercentage - currentDistrictPopulationPercentage);
		return currentMinorityPercentageDifference;
	}
	
	public Districting getDistrictingById(int Id) {
		for (Districting districting: this.districtings) {
			if (districting.getId() == Id)
				return districting;
		}
		return null;
	}

	public Map<Integer, Float> getPrecinctAreaMap() {
		return precinctAreaMap;
	}

	public void setPrecinctAreaMap(Map<Integer, Float> precinctAreaMap) {
		this.precinctAreaMap = precinctAreaMap;
	}
	
	public void generateDistrictingAnalysisSummary() {
		List<Districting> topTenByObjectiveScore = generateTopTenByObjectiveScore();
		List<Districting> highScoringPlansCloseToEnacted = generateHighScoringPlansCloseToEnacted();
		List<Districting> highScoringMajorityMinorityPlans = generatePlansCloseToAverage();
		List<Districting> fiveVeryDifferentPlans = generateVeryDifferentPlans();
		
		DistrictingAnalysisSummary summary = new DistrictingAnalysisSummary();
		summary.setTopTenObjectiveScores(topTenByObjectiveScore);
		summary.setPlansCloseToEnacted(highScoringPlansCloseToEnacted);
		summary.setHighScoringMajMinDistricts(highScoringMajorityMinorityPlans);
		summary.setTopFiveAreaPairDeviations(fiveVeryDifferentPlans);
		this.districtingAnalysisSummary = summary;
	}
	
	public List<Districting> generateTopTenByObjectiveScore() {
		PriorityQueue<Districting> pQueue = new PriorityQueue<Districting>(10, getObjectiveScoreComparator());
		for (Districting districting: this.districtings) {
			if (pQueue.size() < 10) {
				pQueue.add(districting);
			} else {
				float currentScore = districting.getObjectivefunction().getObjScore();
				float minScoreInHeap = pQueue.peek().getObjectivefunction().getObjScore();
				if (currentScore > minScoreInHeap) {
					pQueue.poll();
					pQueue.add(districting);
				}
			}
		}
		List<Districting> topTenList = Arrays.asList(pQueue.toArray(new Districting[0]));
		Collections.sort(topTenList, getObjectiveScoreComparator());
		Collections.reverse(topTenList);
		return topTenList;
	}
	
	Comparator<Districting> getObjectiveScoreComparator() {
	    return new Comparator<Districting>() {
		    @Override
		    public int compare(Districting d1, Districting d2) {
		    	Float d1ObjectiveScore = d1.getObjectivefunction().getObjScore();
		    	Float d2ObjectiveScore = d2.getObjectivefunction().getObjScore();
		        return d1ObjectiveScore.compareTo(d2ObjectiveScore);
		    }
		};
	}
	
	public List<Districting> generateHighScoringPlansCloseToEnacted() {
		PriorityQueue<Districting> pQueue = new PriorityQueue<Districting>(10, Collections.reverseOrder(getDeviationFromEnactedComparator()));
		for (Districting districting: this.districtings) {
			districting.calculateDevFromEnacted(this.enactedDistricting, this.currMinorityPopulation);
			if (pQueue.size() < 10 && districting.getObjectivefunction().getObjScore() > 0.9) {
				pQueue.add(districting);
			} else if (districting.getObjectivefunction().getObjScore() > 0.9) {
				float currentDeviation = districting.getDeviationFromEnacted();
				float maxDeviationInHeap = pQueue.peek().getDeviationFromEnacted();
				if (currentDeviation < maxDeviationInHeap) {
					pQueue.poll();
					pQueue.add(districting);
				}
			}
		}
		List<Districting> topTenList = Arrays.asList(pQueue.toArray(new Districting[0]));
		Collections.sort(topTenList, getDeviationFromEnactedComparator());
		return topTenList;
	}
	
	Comparator<Districting> getDeviationFromEnactedComparator() {
	    return new Comparator<Districting>() {
		    @Override
		    public int compare(Districting d1, Districting d2) {
		    	Float d1ObjectiveScore = d1.getDeviationFromEnacted();
		    	Float d2ObjectiveScore = d2.getDeviationFromEnacted();
		        return d1ObjectiveScore.compareTo(d2ObjectiveScore);
		    }
		};
	}
	
	public List<Districting> generatePlansCloseToAverage() {
		PriorityQueue<Districting> pQueue = new PriorityQueue<Districting>(10, getDeviationFromAverageComparator());
		for (Districting districting: this.districtings) {
			districting.calculateDevFromAvg(this.averageDistricting, this.currMinorityPopulation);
			if (pQueue.size() < 10) {
				pQueue.add(districting);
			} else {
				float currentDeviation = districting.getDeviationFromAverage();
				float maxScoreInHeap = pQueue.peek().getDeviationFromAverage();
				if (currentDeviation < maxScoreInHeap) {
					pQueue.poll();
					pQueue.add(districting);
				}
			}
		}
		List<Districting> topTenList = Arrays.asList(pQueue.toArray(new Districting[0]));
		Collections.sort(topTenList, getDeviationFromAverageComparator());
		return topTenList;
	}
	
	Comparator<Districting> getDeviationFromAverageComparator() {
	    return new Comparator<Districting>() {
		    @Override
		    public int compare(Districting d1, Districting d2) {
		    	Float d1ObjectiveScore = d1.getDeviationFromAverage();
		    	Float d2ObjectiveScore = d2.getDeviationFromAverage();
		        return d1ObjectiveScore.compareTo(d2ObjectiveScore);
		    }
		};
	}
	
	public List<Districting> generateVeryDifferentPlans() {
		List<Districting> fiveVeryDifferentPlans = new ArrayList<Districting>();
		fiveVeryDifferentPlans.add(findClosestDistrictingToPercentile(0));
		fiveVeryDifferentPlans.add(findClosestDistrictingToPercentile(25));
		fiveVeryDifferentPlans.add(this.averageDistricting);
		fiveVeryDifferentPlans.add(findClosestDistrictingToPercentile(75));
		fiveVeryDifferentPlans.add(findClosestDistrictingToPercentile(100));
		return fiveVeryDifferentPlans;
	}
	
	public Districting findClosestDistrictingToPercentile(int percentile) {
		float minimumTotalMinorityPercentageDifference = Float.MAX_VALUE;
		int closestDistrictingId = 0;
		
		for (Districting districting: this.districtings) {
			List<District> districtsList = districting.getDistricts();
			float totalMinorityPercentageDifference = 0;
			
			for (int i = 0; i < districtsList.size(); i++) {
				District currentDistrict = districtsList.get(i);
				totalMinorityPercentageDifference += calculatePercentageDifferenceOfDitrictComparedToPercentile(currentDistrict, i, percentile);
			}
			
			if (totalMinorityPercentageDifference < minimumTotalMinorityPercentageDifference) {
				closestDistrictingId = districting.getId();
				minimumTotalMinorityPercentageDifference = totalMinorityPercentageDifference;
			}
		}
		return getDistrictingById(closestDistrictingId);
	}
	
	private float calculatePercentageDifferenceOfDitrictComparedToPercentile(District district, int index, int percentile) {
		Map<Integer, Float> populationPercentagesMap;
		if (percentile == 0)
			populationPercentagesMap = this.boxAndWhisker.getMinPopDistrict();
		else if (percentile == 25)
			populationPercentagesMap = this.boxAndWhisker.getTwentyFifthPercentile();
		else if (percentile == 75)
			populationPercentagesMap = this.boxAndWhisker.getSeventyFifthPercentile();
		else
			populationPercentagesMap = this.boxAndWhisker.getMaxPopDistrict();

		Map<MinorityPopulation, Float> currentMinorityPopulationPercentagesMap = district.getMinorityPopulationPercentages();
		float districtAveragePopulationPercentage = populationPercentagesMap.get(index);
		float currentDistrictPopulationPercentage = currentMinorityPopulationPercentagesMap.get(this.currMinorityPopulation);
		float currentMinorityPercentageDifference = Math.abs(districtAveragePopulationPercentage - currentDistrictPopulationPercentage);
		return currentMinorityPercentageDifference;
	}
	
	public BoxAndWhisker getBoxAndWhiskerForDistrictingId(int id) {
		BoxAndWhisker boxAndWhisker = this.getBoxAndWhisker();
	    List<District> selectedDistrictsList = this.getDistrictingById(id).getDistricts();
	    int numDistricts = selectedDistrictsList.size();
	    for (int i = 0; i < numDistricts; i++) {
	    	Map<Integer, Float> selectedPercentagesMap = boxAndWhisker.getSelectedDistrictingDots();
	    	District selectedDistrict = selectedDistrictsList.get(i);
	    	float selectedPercentage = selectedDistrict.getMinorityPopulationPercentageForMinority(this.currMinorityPopulation);
	    	selectedPercentagesMap.put(i, selectedPercentage);
	    	boxAndWhisker.setSelectedDistrictingDots(selectedPercentagesMap);
	    }
		return boxAndWhisker;
	}
}
