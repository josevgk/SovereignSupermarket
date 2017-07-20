package com.sovereign.supermarket.model;

import com.google.firebase.database.Exclude;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class Review {

	private String name;
	private String text;
	private String stars;
	private String date;

	public Review() {
	}

	public Review(String name, String text, String stars, String date) {
		this.name = name;
		this.text = text;
		this.stars = stars;
		this.date = date;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	public String getStars() {
		return stars;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Review r = (Review) o;

		return name.equals(r.name) && date.equals(r.date);

	}





	@Exclude
	public Map<String, Object> toMap() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("name", name);
		result.put("text", text);
		result.put("stars", stars);
		result.put("date", date);

		return result;
	}
}