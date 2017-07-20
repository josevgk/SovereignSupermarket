package com.sovereign.supermarket.model;

public abstract class Actor{
	
	// Constructors -----------------------------------------------------------
	public Actor(){
		super();
	}
	
	// Attributes ---------------------------------------------------------
	private String name;
	private String surnames;
	private String email;
	private String phone;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSurnames() {
		return surnames;
	}
	public void setSurnames(String surnames) {
		this.surnames = surnames;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}


}
