package com.restapi.ToTheMoon;

public class Precinct {
	
	private int population;
	private int votingPopulation;
	private float percentBlue;
	private float percentRed;
	private int blackPopulation;
	private int hispanicPopulation;
	private Incumbent incumbent;
	private String geoData;
	
	public Precinct(int population, int votingPopulation, float percentBlue, float percentRed, int blackPopulation,
			int hispanicPopulation, Incumbent incumbent, String geoData) {
		this.population = population;
		this.votingPopulation = votingPopulation;
		this.percentBlue = percentBlue;
		this.percentRed = percentRed;
		this.blackPopulation = blackPopulation;
		this.hispanicPopulation = hispanicPopulation;
		this.incumbent = incumbent;
		this.geoData = geoData;
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
	public int getBlackPopulation() {
		return blackPopulation;
	}
	public void setBlackPopulation(int blackPopulation) {
		this.blackPopulation = blackPopulation;
	}
	public int getHispanicPopulation() {
		return hispanicPopulation;
	}
	public void setHispanicPopulation(int hispanicPopulation) {
		this.hispanicPopulation = hispanicPopulation;
	}
	public Incumbent getIncumbent() {
		return incumbent;
	}
	public void setIncumbent(Incumbent incumbent) {
		this.incumbent = incumbent;
	}
	public String getGeoData() {
		return geoData;
	}
	public void setGeoData(String geoData) {
		this.geoData = geoData;
	}
	
	
}
