package com.sovereign.supermarket.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Consumer {

	// Constructors -----------------------------------------------------------
	public Consumer() {
		super();
	}

	// Attributes ---------------------------------------------------------
	private CreditCard creditCard;
	private String address;
	private String code;
	private String name;
	private String surnames;
	private String email;
	private String phone;
	private String information;

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

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Consumer(String address){
		this.address=address;
	}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public Consumer(String name, String surnames, String phone, String address, String code, String email, String information) {
		this.name = name;
		this.surnames = surnames;
		this.phone = phone;
		this.address=address;
		this.code=code;
		this.email=email;
		this.information=information;
	}
	@Exclude
	public Map<String, Object> toMap() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("name", name);
		result.put("surnames", surnames);
		result.put("phone", phone);
		result.put("address", address);
		result.put("code",code);
		result.put("email",email);
		result.put("information",information);

		return result;
	}
}
