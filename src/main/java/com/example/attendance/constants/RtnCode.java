package com.example.attendance.constants;

public enum RtnCode {
	/* �C�|�Ҧ��i��o�ͪ����~�A�Ȼs�ƿ��~�T���^�� */
	SUCCESSFUL(200, "Successful!"), //
	PARAM_ERROR(400, "Param error!"), //
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
