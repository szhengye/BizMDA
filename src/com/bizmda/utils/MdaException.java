package com.bizmda.utils;

public class MdaException extends Exception {
	public MdaException(String msg) {
		super(msg);
	}
	
	public MdaException(String msg,Throwable cause) {
		super(msg,cause);
	}
}
