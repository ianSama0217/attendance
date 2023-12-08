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

	/* �P�_�а����O�M�e�ݬO�_�۲�(���ۦP������) */
	public static String parser(String type) {
		for (LeaveType item : LeaveType.values()) {
			if (type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return null;
	}

}
