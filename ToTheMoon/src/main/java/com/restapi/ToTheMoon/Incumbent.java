package com.restapi.ToTheMoon;

public class Incumbent {
	
	private Party party;
	private int district;
	private USState state;
	
	public Incumbent(Party party, int district, USState state) {
		this.party = party;
		this.district = district;
		this.state = state;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public int getDistrict() {
		return district;
	}

	public void setDistrict(int district) {
		this.district = district;
	}

	public USState getState() {
		return state;
	}

	public void setState(USState state) {
		this.state = state;
	}
	
	
}
