package com.geeks.mylocker;

import java.io.Serializable;

public class Config implements Serializable {

	public final static String CONTEXT = "com.geeks.mylocker.config.context";
	

	private boolean isLogined = Boolean.FALSE;
	
	private String masterKey;

	public String getMasterKey() {
		return masterKey;
	}

	public void setMasterKey(String masterKey) {
		this.masterKey = masterKey;
	}

	public boolean isLogined() {
		return isLogined;
	}

	public void setLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}
	
	
}
