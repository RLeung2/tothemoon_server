package com.restapi.ToTheMoon;

import java.util.Collection;
import java.util.List;

public class DistrictsBoxAndWhiskerMinorityData {
	private List<Collection<Float>> districtsMinorityData;

	public DistrictsBoxAndWhiskerMinorityData(List<Collection<Float>> districtsMinorityData) {
		this.districtsMinorityData = districtsMinorityData;
	}

	public List<Collection<Float>> getDistrictsMinorityData() {
		return districtsMinorityData;
	}

	public void setDistrictsMinorityData(List<Collection<Float>> districtsMinorityData) {
		this.districtsMinorityData = districtsMinorityData;
	}

}
