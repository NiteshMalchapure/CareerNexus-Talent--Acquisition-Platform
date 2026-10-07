package com.hirehub.hirehub.ExceptionHandler;

public class MethodArgumentNotValidException extends RuntimeException{
	public MethodArgumentNotValidException(String msg) {
		super(msg);
	}

}
