package com.restapi.ToTheMoon;

import java.util.HashMap;
import java.util.Map;

public class BoxAndWhisker {
	
	private MinorityPopulation minorityPopulation;
	private Map<Integer, Float> averagePopDistrict;
	private Map<Integer, Float> minPopDistrict;
	private Map<Integer, Float> maxPopDistrict;
	private Map<Integer, Float> twentyFifthPercentile;
	private Map<Integer, Float> seventyFifthPercentile;
	private Map<Integer, Float> enactedDots;
	private Map<Integer, Float> selectedDistrictingDots;
	
	public BoxAndWhisker() {
		this.averagePopDistrict = new HashMap<Integer, Float>();
		this.minPopDistrict = new HashMap<Integer, Float>();
		this.maxPopDistrict = new HashMap<Integer, Float>();
		this.twentyFifthPercentile = new HashMap<Integer, Float>();
		this.seventyFifthPercentile = new HashMap<Integer, Float>();
		this.enactedDots = new HashMap<Integer, Float>();
		this.selectedDistrictingDots = new HashMap<Integer, Float>();
	}
	
	public BoxAndWhisker(MinorityPopulation minorityPopulation) {
		this.minorityPopulation = minorityPopulation;
		this.averagePopDistrict = new HashMap<Integer, Float>();
		this.minPopDistrict = new HashMap<Integer, Float>();
		this.maxPopDistrict = new HashMap<Integer, Float>();
		this.twentyFifthPercentile = new HashMap<Integer, Float>();
		this.seventyFifthPercentile = new HashMap<Integer, Float>();
		this.enactedDots = new HashMap<Integer, Float>();
		this.selectedDistrictingDots = new HashMap<Integer, Float>();
	}

	public MinorityPopulation getMinorityPopulation() {
		return minorityPopulation;
	}

	public void setMinorityPopulation(MinorityPopulation minorityPopulation) {
		this.minorityPopulation = minorityPopulation;
	}

	public Map<Integer, Float> getAveragePopDistrict() {
		return averagePopDistrict;
	}

	public void setAveragePopDistrict(Map<Integer, Float> averagePopDistrict) {
		this.averagePopDistrict = averagePopDistrict;
	}

	public Map<Integer, Float> getMinPopDistrict() {
		return minPopDistrict;
	}

	public void setMinPopDistrict(Map<Integer, Float> minPopDistrict) {
		this.minPopDistrict = minPopDistrict;
	}

	public Map<Integer, Float> getMaxPopDistrict() {
		return maxPopDistrict;
	}

	public void setMaxPopDistrict(Map<Integer, Float> maxPopDistrict) {
		this.maxPopDistrict = maxPopDistrict;
	}

	public Map<Integer, Float> getTwentyFifthPercentile() {
		return twentyFifthPercentile;
	}

	public void setTwentyFifthPercentile(Map<Integer, Float> twentyFifthPercentile) {
		this.twentyFifthPercentile = twentyFifthPercentile;
	}

	public Map<Integer, Float> getSeventyFifthPercentile() {
		return seventyFifthPercentile;
	}

	public void setSeventyFifthPercentile(Map<Integer, Float> seventyFifthPercentile) {
		this.seventyFifthPercentile = seventyFifthPercentile;
	}

	public Map<Integer, Float> getEnactedDots() {
		return enactedDots;
	}

	public void setEnactedDots(Map<Integer, Float> enactedDots) {
		this.enactedDots = enactedDots;
	}

	public Map<Integer, Float> getSelectedDistrictingDots() {
		return selectedDistrictingDots;
	}

	public void setSelectedDistrictingDots(Map<Integer, Float> selectedDistrictingDots) {
		this.selectedDistrictingDots = selectedDistrictingDots;
	}
	
	
	public void calculateBounds(DistrictsBoxAndWhiskerMinorityData d) {
		return;
	}

}
