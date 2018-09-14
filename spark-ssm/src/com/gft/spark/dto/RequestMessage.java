package com.gft.spark.dto;

public class RequestMessage {

	private String tokenId;
	private String tokenValue;
	private AqUser loginUser;
	private String checkState;
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getTokenValue() {
		return tokenValue;
	}
	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}
	public AqUser getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(AqUser loginUser) {
		this.loginUser = loginUser;
	}
	public String getCheckState() {
		return checkState;
	}
	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	
}
