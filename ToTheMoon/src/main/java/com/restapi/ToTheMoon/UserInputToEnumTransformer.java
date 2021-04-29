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
