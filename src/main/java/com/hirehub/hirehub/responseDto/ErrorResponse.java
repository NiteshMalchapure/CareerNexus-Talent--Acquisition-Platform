package com.hirehub.hirehub.responseDto;

import java.util.Map;

public class ErrorResponse {
	
	private Integer stauscode;
	private String error;
	private String msg;
	private Map<String, String> errors;
	
	
	public ErrorResponse(Integer stauscode, String error, String msg, Map<String, String> errors) {
		
		this.stauscode = stauscode;
		this.error = error;
		this.msg = msg;
		this.errors = errors;
	}

	public ErrorResponse(Integer stauscode, String error, String msg ) {
		
		this.stauscode = stauscode;
		this.error = error;
		this.msg = msg;
	
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	public Integer getStauscode() {
		return stauscode;
	}
	public void setStauscode(Integer stauscode) {
		this.stauscode = stauscode;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
