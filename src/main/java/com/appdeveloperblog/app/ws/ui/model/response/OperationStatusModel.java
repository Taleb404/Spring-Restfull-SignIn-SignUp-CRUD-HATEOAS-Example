package com.appdeveloperblog.app.ws.ui.model.response;

public class OperationStatusModel {

	private String operationResult;
	private String operationName;
	public OperationStatusModel() {}
	public OperationStatusModel(String operationResult, String operationName) {
		this.operationResult = operationResult;
		this.operationName = operationName;
	}
	public String getOperationResult() {
		return operationResult;
	}
	public void setOperationResult(String operationResult) {
		this.operationResult = operationResult;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	
	
}
