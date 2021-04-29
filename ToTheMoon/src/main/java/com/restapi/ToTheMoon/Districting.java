package com.restapi.ToTheMoon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.locationtech.jts.geom.Geometry;

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
	
}
