package com.example.attendance.constants;

public enum LeaveType {

	PERSONNAL("Personal"), //
	SICK("Sick"), //
	OFFICIAL("Official"), //
	ANNUAL("Annual"),//
	;

	private String type;

	private LeaveType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	/* 判斷請假類別和前端是否相符(有相同的項目) */
	public static String parser(String type) {
		for (LeaveType item : LeaveType.values()) {
			if (type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return null;
	}

}
