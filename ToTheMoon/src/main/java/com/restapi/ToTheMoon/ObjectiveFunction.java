package com.restapi.ToTheMoon;

import java.util.HashMap;
import java.util.Map;

public class ObjectiveFunction {
	
	private float objScore;	
	private float enactedAreaScore;
	private float enactedPopScore;
	private float majMinScore;
	private float areaPairDevScore;
	private float splitCountyScore;
	private float popEqScore;
	private float compactnessScore;
	private float devFromAverageScore;
	private Map<Measures, Float> objectiveValues;
	private Map<Measures, Float> objectiveWeights;
	
	public ObjectiveFunction() {
		this.objectiveValues = new HashMap<>();
		this.objectiveWeights = new HashMap<>();
	}
	
	public ObjectiveFunction(float objScore, float enactedAreaScore, float enactedPopScore, float majMinScore,
			float areaPairDevScore, float splitCountyScore, Map<Measures, Float> objectiveValues) {
		this.objScore = objScore;
		this.enactedAreaScore = enactedAreaScore;
		this.enactedPopScore = enactedPopScore;
		this.majMinScore = majMinScore;
		this.areaPairDevScore = areaPairDevScore;
		this.splitCountyScore = splitCountyScore;
		this.objectiveValues = objectiveValues;
	}
	
	public float getObjScore() {
		return objScore;
	}

	public void setObjScore(float objScore) {
		this.objScore = objScore;
	}

	public float getEnactedAreaScore() {
		return enactedAreaScore;
	}

	public void setEnactedAreaScore(float enactedAreaScore) {
		this.enactedAreaScore = enactedAreaScore;
	}

	public float getEnactedPopScore() {
		return enactedPopScore;
	}

	public void setEnactedPopScore(float enactedPopScore) {
		this.enactedPopScore = enactedPopScore;
	}

	public float getMajMinScore() {
		return majMinScore;
	}

	public void setMajMinScore(float majMinScore) {
		this.majMinScore = majMinScore;
	}

	public float getAreaPairDevScore() {
		return areaPairDevScore;
	}

	public void setAreaPairDevScore(float areaPairDevScore) {
		this.areaPairDevScore = areaPairDevScore;
	}

	public float getSplitCountyScore() {
		return splitCountyScore;
	}

	public void setSplitCountyScore(float splitCountyScore) {
		this.splitCountyScore = splitCountyScore;
	}

	public Map<Measures, Float> getObjectiveValues() {
		return objectiveValues;
	}

	public void setObjectiveValues(Map<Measures, Float> objectiveValues) {
		this.objectiveValues = objectiveValues;
	}
	
	public Map<Measures, Float> getObjectiveWeights() {
		return objectiveWeights;
	}

	public void setObjectiveWeights(Map<Measures, Float> objectiveWeights) {
		this.objectiveWeights = objectiveWeights;
	}

	public float getPopEqScore() {
		return popEqScore;
	}

	public void setPopEqScore(float popEqScore) {
		this.popEqScore = popEqScore;
	}

	public float getCompactnessScore() {
		return compactnessScore;
	}

	public void setCompactnessScore(float compactnessScore) {
		this.compactnessScore = compactnessScore;
	}

	public float getDevFromAverageScore() {
		return devFromAverageScore;
	}

	public void setDevFromAverageScore(float devFromAverageScore) {
		this.devFromAverageScore = devFromAverageScore;
	}
	
}
