package com.restapi.ToTheMoon;

import java.util.List;
import java.util.Map;

public class State {

	private USState currState;
	private Job currJob;
	private List<JobSummary> jobSummaries;
	private Job currConstrainedJob;
	// private Districting currDistricting;
	private Districting enactedDistricting;
	private Map<Constraints, Float> currConstraints;
	private MinorityPopulation currMinorityPopulation;
	
	public State(USState state, Districting enactedDistricting, List<JobSummary> jobSummaries) {
		currState = state;
		this.enactedDistricting = enactedDistricting;
		this.jobSummaries = jobSummaries;
	}

	public USState getCurrState() {
		return currState;
	}

	public void setCurrState(USState currState) {
		this.currState = currState;
	}

	public Job getCurrJob() {
		return currJob;
	}

	public void setCurrJob(Job currJob) {
		this.currJob = currJob;
	}

	public List<JobSummary> getJobSummaries() {
		return jobSummaries;
	}

	public void setJobSummaries(List<JobSummary> jobSummaries) {
		this.jobSummaries = jobSummaries;
	}

	public Job getCurrConstrainedJob() {
		return currConstrainedJob;
	}

	public void setCurrConstrainedJob(Job currConstrainedJob) {
		this.currConstrainedJob = currConstrainedJob;
	}

	public Districting getEnactedDistricting() {
		return enactedDistricting;
	}

	public void setEnactedDistricting(Districting enactedDistricting) {
		this.enactedDistricting = enactedDistricting;
	}

	public Map<Constraints, Float> getCurrConstraints() {
		return currConstraints;
	}

	public void setCurrConstraints(Map<Constraints, Float> currConstraints) {
		this.currConstraints = currConstraints;
	}

	public MinorityPopulation getCurrMinorityPopulation() {
		return currMinorityPopulation;
	}

	public void setCurrMinorityPopulation(MinorityPopulation currMinorityPopulation) {
		this.currMinorityPopulation = currMinorityPopulation;
	}
	
	
}
