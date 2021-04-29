package com.restapi.ToTheMoon;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class District {
	
	private Collection<Precinct> precincts;
	private List<Integer> precinctIDs;
	private int population;
	private int votingPopulation;
	private float percentBlue;
	private float percentRed;
	private Map<MinorityPopulation, Integer> minorityPopulations;
	private Map<MinorityPopulation, Float> minorityPopulationPercentages;
	private ObjectiveFunction ObjectiveFunction;
	private int label;
	private String geoData;
	
	public District() {
		
	}
	
	public District(Collection<Precinct> precincts) {
		this.precincts = precincts;
	}


	public Collection<Precinct> getPrecincts() {
		return precincts;
	}


	public void setPrecincts(Collection<Precinct> precincts) {
		this.precincts = precincts;
	}
	
	public List<Integer> getPrecinctIDs() {
		return precinctIDs;
	}

	public void setPrecinctIDs(List<Integer> precinctIDs) {
		this.precinctIDs = precinctIDs;
	}


	public int getPopulation() {
		return population;
	}


	public void setPopulation(int population) {
		this.population = population;
	}


	public int getVotingPopulation() {
		return votingPopulation;
	}


	public void setVotingPopulation(int votingPopulation) {
		this.votingPopulation = votingPopulation;
	}


	public float getPercentBlue() {
		return percentBlue;
	}


	public void setPercentBlue(float percentBlue) {
		this.percentBlue = percentBlue;
	}


	public float getPercentRed() {
		return percentRed;
	}


	public void setPercentRed(float percentRed) {
		this.percentRed = percentRed;
	}


	public Map<MinorityPopulation, Integer> getMinorityPopulations() {
		return minorityPopulations;
	}


	public void setMinorityPopulations(Map<MinorityPopulation, Integer> minorityPopulations) {
		this.minorityPopulations = minorityPopulations;
	}


	public Map<MinorityPopulation, Float> getMinorityPopulationPercentages() {
		return minorityPopulationPercentages;
	}


	public void setMinorityPopulationPercentages(Map<MinorityPopulation, Float> minorityPopulationPercentages) {
		this.minorityPopulationPercentages = minorityPopulationPercentages;
	}


	public ObjectiveFunction getObjectiveFunction() {
		return ObjectiveFunction;
	}


	public void setObjectiveFunction(ObjectiveFunction objectiveFunction) {
		ObjectiveFunction = objectiveFunction;
	}


	public int getLabel() {
		return label;
	}


	public void setLabel(int label) {
		this.label = label;
	}


	public String getGeoData() {
		return geoData;
	}


	public void setGeoData(String geoData) {
		this.geoData = geoData;
	}
	
	

}
