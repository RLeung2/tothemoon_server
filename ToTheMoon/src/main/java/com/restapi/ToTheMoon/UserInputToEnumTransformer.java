package com.restapi.ToTheMoon;

public class UserInputToEnumTransformer {
	
	public static USState userStateToEnum(String state) {
		if (state.equals("nevada")) {
			return USState.NV;
		} else if (state.equals("new_york")) {
			return USState.NY;
		} else {
			return USState.WA;
		}
	}
}
