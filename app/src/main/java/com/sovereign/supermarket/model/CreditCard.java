package com.sovereign.supermarket.model;

public class CreditCard{
	
	// Constructors -----------------------------------------------------------
	public CreditCard(){
		super();
	}
	
	// Attributes ---------------------------------------------------------
	private String holder;
	private String brandName;
	private String number;
	private int month;
	private int year;
	private int cvvCode;

	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}

	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	public int getCvvCode() {
		return cvvCode;
	}
	public void setCvvCode(int cvvCode) {
		this.cvvCode = cvvCode;
	}

}
