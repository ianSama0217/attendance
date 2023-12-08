package com.example.attendance.constants;

public enum ReviewType {

	PASS("Pass"), //
	REVIEWING("Reviewing"), //
	REJECT("Reject"), //
	REVOKE("Revoke"),//
	;

	private String type;

	private ReviewType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	/* 判斷請假類別和前端是否相符(有相同的項目) */
	public static String parser(String type) {
		for (ReviewType item : ReviewType.values()) {
			if (type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return null;
	}

}
