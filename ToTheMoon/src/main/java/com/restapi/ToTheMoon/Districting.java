package com.restapi.ToTheMoon;

import java.util.Collection;

public class Districting {

	private int id;
	private ObjectiveFunction objectivefunction;
	private Collection<District> districts;
	
	public Districting(int id, ObjectiveFunction objectivefunction, Collection<District> districts) {
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

	public Collection<District> getDistricts() {
		return districts;
	}

	public void setDistricts(Collection<District> districts) {
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
	
	public float calculateEfficiencyGap() {
		return (float) 0.0;
	}
	
	public void gillConstructRenumbering() {
		return;
	}
	
}
