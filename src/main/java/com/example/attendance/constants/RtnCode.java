package com.example.attendance.constants;

public enum RtnCode {
	/* 列舉所有可能發生的錯誤，客製化錯誤訊息回傳 */
	SUCCESSFUL(200, "Successful!"), //
	PARAM_ERROR(400, "Param error!"), //
	ID_HAS_EXISTED(400, "ID has existed!"), //
	DEPARTMENT_NOT_FOUND(404, "Department not found!"), //
	;

	private int code;

	private String message;

	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
