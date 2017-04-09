package com.soak.system.constant;

public enum ObjectType {

	ROLE("角色"), ORG("机构"), USER("用户");
	
	private String message;

	ObjectType(String message) {
		this.message = message;
	}

	public String toString() {
		return this.message;
	}
}
