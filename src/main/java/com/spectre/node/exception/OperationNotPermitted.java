package com.spectre.node.exception;

public class OperationNotPermitted extends RuntimeException {
	public OperationNotPermitted(String message) {
		super(message);
	}
}
