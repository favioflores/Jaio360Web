package com.jaio360.exception;

public class ExecutorMovementException extends Exception {
	
	private String exceptionMsg;

	public ExecutorMovementException(String exceptionMsg) {
		super();
		this.exceptionMsg = exceptionMsg;
	}
	
	public String getExceptionMsg() {
		return exceptionMsg;
	}

}