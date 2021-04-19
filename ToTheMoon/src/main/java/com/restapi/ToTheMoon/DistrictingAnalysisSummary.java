package com.restapi.ToTheMoon;

import java.util.List;

public class DistrictingAnalysisSummary {
	
	private List<Districting> topTenObjectiveScores;
	private List<Districting> plansCloseToEnacted;
	private List<Districting> highScoringMajMinDistricts;
	private List<Districting> topFiveAreaPairDeviations;
	
	public DistrictingAnalysisSummary() {
		
	}

	public List<Districting> getTopTenObjectiveScores() {
		return topTenObjectiveScores;
	}

	public void setTopTenObjectiveScores(List<Districting> topTenObjectiveScores) {
		this.topTenObjectiveScores = topTenObjectiveScores;
	}

	public List<Districting> getPlansCloseToEnacted() {
		return plansCloseToEnacted;
	}

	public void setPlansCloseToEnacted(List<Districting> plansCloseToEnacted) {
		this.plansCloseToEnacted = plansCloseToEnacted;
	}

	public List<Districting> getHighScoringMajMinDistricts() {
		return highScoringMajMinDistricts;
	}

	public void setHighScoringMajMinDistricts(List<Districting> highScoringMajMinDistricts) {
		this.highScoringMajMinDistricts = highScoringMajMinDistricts;
	}

	public List<Districting> getTopFiveAreaPairDeviations() {
		return topFiveAreaPairDeviations;
	}

	public void setTopFiveAreaPairDeviations(List<Districting> topFiveAreaPairDeviations) {
		this.topFiveAreaPairDeviations = topFiveAreaPairDeviations;
	}
	
}
