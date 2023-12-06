package com.example.attendance.constants;

public enum RtnCode {
	/* 列舉所有可能發生的錯誤，客製化錯誤訊息回傳 */
	SUCCESSFUL(200, "Successful!"), //
	PARAM_ERROR(400, "Param error!"), //
	ID_HAS_EXISTED(400, "ID has existed!"), //
	DEPARTMENT_NOT_FOUND(404, "Department not found!"), //
	ID_NOT_FOUND(404, "ID not found!"), //
	PASSWORD_ERROR(400, "Password error!"), //
	EMPLOYEE_CREATE_ERROR(400, "Employee create error!"), //
	LOGIN_FIRST(400, "Login first!"), //
	UNAUTHORIZATED(401, "Unauthorizated!"), //
	CHANGE_PASSWORD_ERROR(400, "Change password error!"), //
	OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENTICAL(400, "Old password and new password are identical!"), //
	FORGOT_PASSWORD_ERROR(400, "Forgot password error!"), //
	AUTH_CODE_NOT_MATCHED(400, "Auth code not matches!"), //
	AUTH_CODE_EXPIRED(400, "Auth code expired!"), //
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
