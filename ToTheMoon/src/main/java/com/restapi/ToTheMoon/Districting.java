package com.restapi.ToTheMoon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.util.Pair;
import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;
import org.jgrapht.util.*;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.MatchingAlgorithm.Matching;
import org.jgrapht.alg.matching.MaximumWeightBipartiteMatching;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.operation.union.UnaryUnionOp;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings({ "unused" })
public class Districting {

	private int id;
	private ObjectiveFunction objectiveFunction; 
	private List<District> districts;
	private float deviationFromEnacted;
	private float deviationFromAverage;
	private int majorityMinorityDistricts;


	public Districting() {
		
	}
	
	public Districting(int id, ObjectiveFunction objectivefunction, List<District> districts) {
		super();
		this.id = id;
		this.objectiveFunction = objectivefunction;
		this.districts = districts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ObjectiveFunction getObjectivefunction() {
		return objectiveFunction;
	}

	public void setObjectivefunction(ObjectiveFunction objectivefunction) {
		this.objectiveFunction = objectivefunction;
	}

	public List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}
	
	public float getDeviationFromEnacted() {
		return deviationFromEnacted;
	}

	public void setDeviationFromEnacted(float deviationFromEnacted) {
		this.deviationFromEnacted = deviationFromEnacted;
	}

	public float getDeviationFromAverage() {
		return deviationFromAverage;
	}

	public void setDeviationFromAverage(float deviationFromAverage) {
		this.deviationFromAverage = deviationFromAverage;
	}
	
	public int getMajorityMinorityDistricts() {
		return majorityMinorityDistricts;
	}

	public void setMajorityMinorityDistricts(int majorityMinorityDistricts) {
		this.majorityMinorityDistricts = majorityMinorityDistricts;
	}

	/*All the methods below will be methods that do actual calculations 
	 * */
	public void calculateDevFromAvg(Districting averageDistricting, MinorityPopulation minority) {
		float avgScore = calculateDeviationFromComparedDistricting(averageDistricting, minority, true);
		this.setDeviationFromAverage(avgScore);
		this.objectiveFunction.setDevFromAverageScore(avgScore);
	}
	
	public void calculateDevFromEnacted(Districting enactedDistricting, MinorityPopulation minority) {
		float devEnactedPopScore = calculateDeviationFromComparedDistricting(enactedDistricting, minority, false);
		this.setDeviationFromEnacted(devEnactedPopScore);
		this.objectiveFunction.setEnactedPopScore(devEnactedPopScore);
	}
	
	public float calculateSplitCountyScore() {
		return (float) 0.0;
	}
	
	public void calculateObjScore(float popEqWeight, float compactnessWeight, float devFromAvgWeight, 
			float devFromEnactedPopWeight, float devFromEnactedAreaWeight) {
		for (int i = 0; i < this.districts.size(); i++) {
			District currDistrict = this.districts.get(i);
			float popEqValueDistrict = popEqWeight * currDistrict.getObjectiveFunction().getPopEqScore();
			float compactnessValueDistrict = compactnessWeight * currDistrict.getObjectiveFunction().getCompactnessScore();
			float devAvgValueDistrict = devFromAvgWeight * currDistrict.getObjectiveFunction().getDevFromAverageScore();
			float devEnactedPopValueDistrict = devFromEnactedPopWeight * currDistrict.getObjectiveFunction().getEnactedPopScore();
			float devEnactedAreaValueDistrict = devFromEnactedAreaWeight * currDistrict.getObjectiveFunction().getEnactedAreaScore();
			
			Map<Measures, Float> objectiveValues = currDistrict.getObjectiveFunction().getObjectiveValues();
			objectiveValues.put(Measures.POPULATION_EQUALITY, popEqValueDistrict);
			objectiveValues.put(Measures.GRAPH_COMPACTNESS, compactnessValueDistrict);
			objectiveValues.put(Measures.DEVIATION_FROM_AVG, devAvgValueDistrict);
			objectiveValues.put(Measures.DEVIATION_FROM_ENACTED_POPULATION, devEnactedPopValueDistrict);
			objectiveValues.put(Measures.DEVIATION_FROM_ENACTED_AREA, devEnactedAreaValueDistrict);
		}
		float popEqValueDistrict = popEqWeight * this.objectiveFunction.getPopEqScore();
		float compactnessValueDistrict = compactnessWeight * this.objectiveFunction.getCompactnessScore();
		float devAvgValueDistrict = devFromAvgWeight * this.objectiveFunction.getDevFromAverageScore();
		float devEnactedPopValueDistrict = devFromEnactedPopWeight * this.objectiveFunction.getEnactedPopScore();
		float devEnactedAreaValueDistrict = devFromEnactedAreaWeight * this.objectiveFunction.getEnactedAreaScore();
		
		Map<Measures, Float> districtingObjectiveValues = this.objectiveFunction.getObjectiveValues();
		districtingObjectiveValues.put(Measures.POPULATION_EQUALITY, popEqValueDistrict);
		districtingObjectiveValues.put(Measures.GRAPH_COMPACTNESS, compactnessValueDistrict);
		districtingObjectiveValues.put(Measures.DEVIATION_FROM_AVG, devAvgValueDistrict);
		districtingObjectiveValues.put(Measures.DEVIATION_FROM_ENACTED_POPULATION, this.deviationFromEnacted);
		districtingObjectiveValues.put(Measures.DEVIATION_FROM_ENACTED_AREA, devEnactedAreaValueDistrict);
		
		this.objectiveFunction.setObjScore(popEqValueDistrict + compactnessValueDistrict + devAvgValueDistrict + devEnactedPopValueDistrict + devEnactedAreaValueDistrict);
	}
	
	public void calculateEnactedAreaScore(Districting enacted) {
		List<District> enactedDistricts = enacted.getDistricts();
		float currScore = 0;
		for (int i = 0; i < enactedDistricts.size(); i++) {
			float enactedCurrDistrictArea = enactedDistricts.get(i).getArea();
			float currDistrictArea = this.districts.get(i).calculateArea();
			
			float areaDeviation = this.calculatePercentDeviation(enactedCurrDistrictArea, currDistrictArea);

			currScore += areaDeviation;
			
			this.districts.get(i).getObjectiveFunction().setEnactedAreaScore(areaDeviation);
		}
		currScore = currScore / this.districts.size();
		this.objectiveFunction.setEnactedAreaScore(currScore);
	}
	
	public float calculateMajMinScore() {
		return (float) 0.0;
	}
	
	public float calculateAreaPairDevScore() {
		return (float) 0.0;
	}
	
	public void calculatePopEqualityScore() {
		int totalPop = 0;
		float idealPop = 0;
		for (int i = 0; i < this.districts.size(); i++) {
			District currDistrict = this.districts.get(i);
			totalPop += currDistrict.getPopulation();
		}
		
		idealPop = (float)(totalPop / this.districts.size());
		
		float totalPopEqScore = 0;
		for (int i = 0; i < this.districts.size(); i++) {
			District currDistrict = this.districts.get(i);
			float popEqScore = calculatePercentDeviation(idealPop, (float) currDistrict.getPopulation());
			totalPopEqScore += popEqScore;
			currDistrict.getObjectiveFunction().setPopEqScore(popEqScore);
		}
		
		totalPopEqScore = totalPopEqScore / this.districts.size();
		this.objectiveFunction.setPopEqScore(totalPopEqScore);
	}
	
	private static List<Float> calculatePolsbyPopperScore(List<Geometry> districtGeometryList) {
		List<Float> polsbyPopperScores = new ArrayList<Float>();
		
		for (int i = 0; i < districtGeometryList.size(); i++) {
			Geometry currDistrictGeometry = districtGeometryList.get(i);
			double currDistrictArea = currDistrictGeometry.getArea();
			double currDistrictPerimeter = currDistrictGeometry.getLength();
			
			float ppScore = (float) ((4 * Math.PI * currDistrictArea) / (Math.pow(currDistrictPerimeter, 2)));
			polsbyPopperScores.add(ppScore);
		}
		return polsbyPopperScores;
	}
	
	public float calculateEfficiencyGap() {
		return (float) 0.0;
	}
	
	public void gillConstructRenumbering(Districting enactedDistricting) {
		List<District> enactedDistrictsList = enactedDistricting.getDistricts();
		Graph<District, DefaultWeightedEdge> bipartiteGraph = generateBipartiteGraph(enactedDistrictsList);
		Set<District> enactedDistrictsSet = new HashSet<District>(enactedDistrictsList);
		Set<District> currentDistrictsSet = new HashSet<District>(this.districts);
		MaximumWeightBipartiteMatching<District, DefaultWeightedEdge> bipartiteMatcher = 
				new MaximumWeightBipartiteMatching<District, DefaultWeightedEdge>(bipartiteGraph, enactedDistrictsSet, currentDistrictsSet);
		Matching<District, DefaultWeightedEdge> bestMatching = bipartiteMatcher.getMatching();
		HashMap<District, District> reorderedMapping = generateReorderedMapping(bestMatching);
		reorderDistricts(reorderedMapping, enactedDistrictsList);
	}
	
	private Graph<District, DefaultWeightedEdge> generateBipartiteGraph(List<District> enactedDistrictsList) {
		HashMap<Pair<District, District>, Integer> intersectionMap = generateIntersectionMap(enactedDistrictsList);
		Graph<District, DefaultWeightedEdge> bipartiteGraph = new WeightedMultigraph<District, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		for(int i = 0; i < enactedDistrictsList.size(); i++) {
			bipartiteGraph.addVertex(enactedDistrictsList.get(i));
			bipartiteGraph.addVertex(this.districts.get(i));
		}
		for (Map.Entry<Pair<District, District>, Integer> mapEntry : intersectionMap.entrySet()) {
			Pair<District, District> districtPair = mapEntry.getKey();
			int intersectionPopulation = mapEntry.getValue();
			//double intersectionEdgeWeight = intersectionPopulation != 0 ? 1 / (double)intersectionPopulation : 99999999.0;
			double intersectionEdgeWeight = intersectionPopulation;
			DefaultWeightedEdge edge = bipartiteGraph.addEdge(districtPair.getKey(), districtPair.getValue());
			bipartiteGraph.setEdgeWeight(edge, intersectionEdgeWeight);
		}
		return bipartiteGraph;
	}
	
	private HashMap<Pair<District, District>, Integer> generateIntersectionMap(List<District> enactedDistrictsList) {
		HashMap<Pair<District, District>, Integer> intersectionMap = new HashMap<Pair<District, District>, Integer>();
		
		for (int i = 0; i < enactedDistrictsList.size(); i++) {
			District enactedDistrict = enactedDistrictsList.get(i);
			Collection<Precinct> enactedPrecincts = enactedDistrict.getPrecincts();

			for (int j = 0; j < this.districts.size(); j++) {
				District comparedDistrict = this.districts.get(j);
				Collection<Precinct> comparedPrecincts = comparedDistrict.getPrecincts();
				Collection<Precinct> commonPrecincts = this.getCommonPrecincts(enactedPrecincts, comparedPrecincts);	
				int intersectionPopulation = 0;
				for (Precinct precinct: commonPrecincts) {
					intersectionPopulation += precinct.getPopulation();
				}
				Pair<District, District> districtPair = new Pair<District, District>(enactedDistrict, comparedDistrict);
				intersectionMap.put(districtPair, intersectionPopulation);
			}
		}
		return intersectionMap;
	}
	
	private Collection<Precinct> getCommonPrecincts(Collection<Precinct> enactedPrecincts, Collection<Precinct> comparedPrecincts) {
		Collection<Precinct> commonPrecincts = new ArrayList<Precinct>();
		for (Precinct enactedPrecinct: enactedPrecincts) {
			for (Precinct comparedPrecinct: comparedPrecincts) {
				if (enactedPrecinct.getId() == comparedPrecinct.getId())
						commonPrecincts.add(enactedPrecinct);
			}
		}
		return commonPrecincts;
	}
	
	private HashMap<District, District> generateReorderedMapping(Matching<District, DefaultWeightedEdge> matching) {
		Graph<District, DefaultWeightedEdge> bipartiteGraph = matching.getGraph();
		HashMap<District, District> reorderedMapping = new HashMap<District, District>();
		Set<DefaultWeightedEdge> bestEdges = matching.getEdges();
		Iterator<DefaultWeightedEdge> set = bestEdges.iterator();
		while(set.hasNext()){
			DefaultWeightedEdge edge = set.next();
			District enactedSource = bipartiteGraph.getEdgeSource(edge);
			District currentTarget = bipartiteGraph.getEdgeTarget(edge);
			reorderedMapping.put(enactedSource, currentTarget);
	     }
		return reorderedMapping;
	}
	
	private void reorderDistricts(HashMap<District, District> reorderedMapping, List<District> enactedDistrictsList) {
		List<District> reorderedDistrictsList= new ArrayList<District>();
		for (int i = 0; i < enactedDistrictsList.size(); i++) {
			District currentDistrict = reorderedMapping.get(enactedDistrictsList.get(i));
			currentDistrict.setLabel(i);
			reorderedDistrictsList.add(currentDistrict);
		}
		this.setDistricts(reorderedDistrictsList);
	}
	
	public void sortDistrictsByMinority(MinorityPopulation minority) {
		Collections.sort(this.districts, getMinorityPercentageComparator(minority));
	}
	
	Comparator<District> getMinorityPercentageComparator(final MinorityPopulation minority) {
	    return new Comparator<District>() {
		    @Override
		    public int compare(District d1, District d2) {
		    	Float d1Percentage = d1.getMinorityPopulationPercentages().get(minority);
		    	Float d2Percentage = d2.getMinorityPopulationPercentages().get(minority);
		        return d1Percentage.compareTo(d2Percentage);
		    }
		};
	}
	
	public String generateDistrictingGeoJSON(String fileName, Districting selectedDistricting) throws JsonGenerationException, JsonMappingException, IOException {
		// Documentation requires properties to be of these types to serialize properly into GeoJSON
		List<Feature> features = new ArrayList<Feature>();
		Map<String, Object> properties = new HashMap<String, Object>();
		GeoJSONWriter writer = new GeoJSONWriter();
		ObjectMapper mapper = new ObjectMapper();
		
		List<Geometry> precinctGeometry = generateJTSPrecinctGeometry(fileName);
		List<Geometry> districtingsGeodata = constructDistricting(selectedDistricting, precinctGeometry);
		
		for (int i = 0; i < districtingsGeodata.size(); i++) {
			GeoJSON json = writer.write(districtingsGeodata.get(i));
			// This path to Geometry is used since the JTS library uses a class of the same name
			features.add(new Feature((org.wololo.geojson.Geometry) json, properties));
		}
		
		GeoJSON finalJSON = writer.write(features);
		return mapper.writeValueAsString(finalJSON);
	}
	
	private List<Geometry> generateJTSPrecinctGeometry(String fileName) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		// Configured this way to ignore the CRS field of the GeoJSON which the FeatureColection class does not have (nor do we need it)
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		FeatureCollection featureCollection = objectMapper.readValue(new File(fileName), FeatureCollection.class);
		GeoJSONReader reader = new GeoJSONReader();
		Feature[] features = featureCollection.getFeatures();
		List<Geometry> precinctGeometry = new ArrayList<Geometry>();
		
		for (int i = 0; i < features.length; i++) {
			Geometry geometry = reader.read(features[i].getGeometry());
			Geometry fixedGeometry = geometry.buffer(0);
			precinctGeometry.add(fixedGeometry);
		}
		return precinctGeometry;
	}
	
	public List<Geometry> constructDistricting(Districting districting, List<Geometry> precinctGeometry) {
		List<District> districtsList = districting.getDistricts();
		List<Geometry> districtingsGeodata = new ArrayList<Geometry>();
		
		for(int i = 0; i < districtsList.size(); i++) {
			districtingsGeodata.add(generateDistrictGeodata(districtsList.get(i), precinctGeometry));
		}
		return districtingsGeodata;
	}

	public Geometry generateDistrictGeodata(District district, List<Geometry> precinctGeometry) {
		List<Long> precinctsInDistrict = district.getPrecinctIDs();
		List<Geometry> precinctPolygons = new ArrayList<Geometry>();
		
		for (int i = 0; i < precinctsInDistrict.size(); i++) {
			int precinctIndex = Integer.parseInt(precinctsInDistrict.get(i).toString()) - 1;
			precinctPolygons.add(precinctGeometry.get(precinctIndex));
		}

		Geometry combinedPrecincts = UnaryUnionOp.union(precinctPolygons);
		return combinedPrecincts;
	}
	
	public float calculateDeviationFromComparedDistricting(Districting comparedDistricting, MinorityPopulation minority, boolean isAverage) {
		List<District> comparedDistrictsList = comparedDistricting.getDistricts();
		
		float totalDeviationSum = 0;
		for (int i = 0; i < comparedDistrictsList.size(); i++) {
			District currentDistrict = this.districts.get(i);
			District comparedDistrict = comparedDistrictsList.get(i);
			Map<MinorityPopulation, Float> currentMinorityPercentagesMap = currentDistrict.getMinorityPopulationPercentages();
			Map<MinorityPopulation, Float> comparedMinorityPercentagesMap = comparedDistrict.getMinorityPopulationPercentages();
			float currentMinorityPercentage = currentMinorityPercentagesMap.get(minority);
			float comparedMinorityPercentage = comparedMinorityPercentagesMap.get(minority);
			float currentDeviation = calculatePercentDeviation(currentMinorityPercentage, comparedMinorityPercentage);
			totalDeviationSum += currentDeviation;
			if (isAverage) {
				currentDistrict.getObjectiveFunction().setDevFromAverageScore(currentDeviation);
			} else {
				currentDistrict.getObjectiveFunction().setEnactedPopScore(currentDeviation);
			}
		}
		return (totalDeviationSum / comparedDistrictsList.size());
	}
	
	private float calculatePercentDeviation(float actual, float observed) {
		float difference = Math.abs(observed - actual);
		return difference / actual;
	}
	
	public void generateObjectiveFunction() {
		ObjectiveFunction objectiveFunction = new ObjectiveFunction();
		objectiveFunction.setObjScore((float) Math.random());
		
		this.setObjectivefunction(objectiveFunction);
	}
}
