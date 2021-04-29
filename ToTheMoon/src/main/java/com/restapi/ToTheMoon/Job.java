package com.restapi.ToTheMoon;

import java.util.Collection;
import java.util.Map;

public class Job {
	
	private Collection<Districting> districtings;
	private float populationEquality;
	private MinorityPopulation currMinorityPopulation;
	private DistrictingAnalysisSummary districtingAnalysisSummary;
	private String id;
	private BoxAndWhisker boxAndWhisker;
	
	public Job() {
		
	}

	public Job constrain(Map<Constraints, Float> filtersMap) {
		return null;
		
	}
	
	public void generateBoxAndWhiskerData() {
		
	}
	
	public void instantiateObjectiveScores(Map<Measures, Float> measuresMap, Map<Constraints, Float> constraintsMap) {
		
	}
	
	public void generateDistrictingAnalysisSummary(DistrictingAnalysisSummary districtingAnalysisSummary) {
		
	}
	
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
	
}
