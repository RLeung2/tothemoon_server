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

public class Districting {

	private int id;
	private ObjectiveFunction objectivefunction;
	private List<District> districts;
	
	public Districting() {
		
	}
	
	public Districting(int id, ObjectiveFunction objectivefunction, List<District> districts) {
		super();
		this.id = id;
		this.objectivefunction = objectivefunction;
		this.districts = districts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ObjectiveFunction getObjectivefunction() {
		return objectivefunction;
	}

	public void setObjectivefunction(ObjectiveFunction objectivefunction) {
		this.objectivefunction = objectivefunction;
	}

	public List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}
	
	/*All the methods below will be methods that do actual calculations 
	 * */
	public float calculateDevFromAvg() {
		return (float) 0.0;
	}
	
	public float calculateSplitCountyScore() {
		return (float) 0.0;
	}
	
	public float calculateObjScore() {
		return (float) 0.0;
	}
	
	public float calculateEnactedAreaScore() {
		return (float) 0.0;
	}
	
	public float calculateEnactedPopScore() {
		return (float) 0.0;
	}
	
	public float calculateMajMinScore() {
		return (float) 0.0;
	}
	
	public float calculateAreaPairDevScore() {
		return (float) 0.0;
	}
	
	public float calculatePopEqualityScore() {
		return (float) 0.0;
	}
	
	public float calculateCompactness(Constraints compactnessType) {
		return (float) 0.0;
	}
	
	private static List<Float> calculatePolsbyPopperScore(List<Geometry> districtGeometryList) {
		List<Float> polsbyPopperScores = new ArrayList<>();
		
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
	
	public void gillConstructRenumbering() {
		return;
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
		List<Integer> precinctsInDistrict = district.getPrecinctIDs();
		List<Geometry> precinctPolygons = new ArrayList<Geometry>();
		
		for (int i = 0; i < precinctsInDistrict.size(); i++) {
			precinctPolygons.add(precinctGeometry.get(i));
		}

		Geometry combinedPrecincts = UnaryUnionOp.union(precinctPolygons);
		return combinedPrecincts;
	}
	
}
