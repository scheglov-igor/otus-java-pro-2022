package ru.otusproject.exception;

public class CriticalStopException extends Exception {

	public CriticalStopException(Throwable cause) {
		super(cause);
	}

	public CriticalStopException(String message) {
		super(message);
	}

	public CriticalStopException(String message, Throwable cause) {
		super(message, cause);
	}

}