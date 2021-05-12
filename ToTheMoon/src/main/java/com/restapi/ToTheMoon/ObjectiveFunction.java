package com.restapi.ToTheMoon;

import java.util.Map;

public class ObjectiveFunction {
	
	private float objScore;	
	private float enactedAreaScore;
	private float enactedPopScore;
	private float majMinScore;
	private float areaPairDevScore;
	private float splitCountyScore;
	private Map<String, Float> objectiveValues;
	
	public ObjectiveFunction() {
		
	}
	
	public ObjectiveFunction(float objScore, float enactedAreaScore, float enactedPopScore, float majMinScore,
			float areaPairDevScore, float splitCountyScore, Map<String, Float> objectiveValues) {
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

	public Map<String, Float> getObjectiveValues() {
		return objectiveValues;
	}

	public void setObjectiveValues(Map<String, Float> objectiveValues) {
		this.objectiveValues = objectiveValues;
	}
	
}
