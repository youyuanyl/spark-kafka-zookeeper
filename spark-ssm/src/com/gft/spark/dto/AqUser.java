package com.gft.spark.dto;

public class AqUser {

	private String id;
	private String username;
	private String password;
	private String name;
	private String scrq;
	private String type;
	
	public AqUser(){
		
	}

	public AqUser(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScrq() {
		return scrq;
	}
	public void setScrq(String scrq) {
		this.scrq = scrq;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
