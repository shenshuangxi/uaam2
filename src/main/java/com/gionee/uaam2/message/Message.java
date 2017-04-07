package com.gionee.uaam2.message;

public class Message {

	private Boolean isSuccess;
	private String message;
	private Object entity;
	
	public Message(Boolean isSuccess, String message) {
		this.isSuccess = isSuccess;
		this.message = message;
	}
	
	
	
	public Message(Boolean isSuccess, String message, Object entity) {
		this(isSuccess, message);
		this.entity = entity;
	}



	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}
	
	
	
	
}
