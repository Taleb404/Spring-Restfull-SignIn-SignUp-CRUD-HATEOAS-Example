package com.appdeveloperblog.app.ws.ui.model.response;

public enum ErrorMessages {
	MISSING_REQUIRED_FIELD("missing required field. please check documentaion for required fields."),
	RECORD_ALREADY_EXISTS("record alredy exists"),
	INTERNAL_SERVER_ERROR("Internal Server Error"),
	NO_RECORD_FOUND("record with provided Id is not found!"),
	AUTHENTICATION_FAILED("Authentication failed."),
	COULD_NOT_UPDATE_RECORD("could not update record"),
	COULD_NOT_DELETE_RECORD("could not delete record"),
	EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified");
	
	private String errorMessage;

	private ErrorMessages(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
}
