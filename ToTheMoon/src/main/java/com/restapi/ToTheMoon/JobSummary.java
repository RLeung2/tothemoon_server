package com.restapi.ToTheMoon;

public class JobSummary {
	
	private USState state;
	private int numDistrictings;
	private float populationEquality;
	private int rounds;
	private int coolingPeriod;
	private int jobId;
//	private int recom;
	
	public JobSummary(USState state, int numDistrictings, float populationEquality, int rounds, int coolingPeriod,
			int jobId) {
		super();
		this.state = state;
		this.numDistrictings = numDistrictings;
		this.populationEquality = populationEquality;
		this.rounds = rounds;
		this.coolingPeriod = coolingPeriod;
		this.jobId = jobId;
	}

	public JobSummary() {
	
	}

	public USState getState() {
		return state;
	}

	public void setState(USState state) {
		this.state = state;
	}

	public int getNumDistrictings() {
		return numDistrictings;
	}

	public void setNumDistrictings(int numDistrictings) {
		this.numDistrictings = numDistrictings;
	}

	public float getPopulationEquality() {
		return populationEquality;
	}

	public void setPopulationEquality(float populationEquality) {
		this.populationEquality = populationEquality;
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public int getCoolingPeriod() {
		return coolingPeriod;
	}

	public void setCoolingPeriod(int coolingPeriod) {
		this.coolingPeriod = coolingPeriod;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	
//	public int getRecom() {
//		return recom;
//	}
//
//	public void setRecom(int newRecom) {
//		this.recom = newRecom;
//	}
	
}
