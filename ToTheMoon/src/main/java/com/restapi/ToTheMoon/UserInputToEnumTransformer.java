package com.restapi.ToTheMoon;

public class UserInputToEnumTransformer {
	
	public static USState transformUserStateToEnum(String state) {
		if (state.equals("nevada")) {
			return USState.NV;
		} else if (state.equals("new_york")) {
			return USState.NY;
		} else {
			return USState.WA;
		}
	}
	
	public static String transformUserStateToStateGeoJsonFilePath(String state) {
		if (state.equals("nevada")) {
			return "C:\\Users\\Ahmed\\git\\tothemoon\\ToTheMoon\\src\\main\\java\\DistrictingData\\nvDistricts.json";
		} else if (state.equals("new_york")) {
			return "C:\\Users\\Ahmed\\git\\tothemoon\\ToTheMoon\\src\\main\\java\\DistrictingData\\nyDistricts.json";
		} else {
			return "C:\\Users\\Ahmed\\git\\tothemoon\\ToTheMoon\\src\\main\\java\\DistrictingData\\waDistricts.json";
		}
	}
	
	public static MinorityPopulation userMinorityPopToEnum(String minorityPop) {
		if (minorityPop.equals("african_american")) {
			return MinorityPopulation.AFRICAN_AMERICAN;
		} else if (minorityPop.equals("hispanic")) {
			return MinorityPopulation.HISPANIC;
		} else {
			return MinorityPopulation.ASIAN;
		}
	}
}
