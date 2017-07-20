package com.sovereign.supermarket.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Item {

	private String sku;
	private String name;
	private String description;
	private double price;
	private String picture;
	private boolean deleted;
	private String category;

	public Item() {
	}

	public Item(String sku, String name, String description, double price, String picture, boolean deleted) {
		this.sku = sku;
		this.name = name;
		this.description = description;
		this.price = price;
		this.picture = picture;
		this.deleted = deleted;
	}

	public Item(String sku, String name, String description, double price, String picture, boolean deleted, String category) {
		this.sku = sku;
		this.name = name;
		this.description = description;
		this.price = price;
		this.picture = picture;
		this.deleted = deleted;
		this.category=category;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Item item = (Item) o;

		return sku.equals(item.sku);

	}

	@Override
	public int hashCode() {
		return sku.hashCode();
	}

	@Override
	public String toString() {
		return "Item{" +
				"sku='" + sku + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", price=" + price +
				", picture='" + picture + '\'' +
				", deleted=" + deleted +
				'}';
	}

	public Item(String name, String description, Double price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}


	@Exclude
	public Map<String, Object> toMap() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("name", name);
		result.put("description", description);
		result.put("price", price);

		return result;
	}

	@Exclude
	public Map<String, Object> createNewToMap() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("name", name);
		result.put("description", description);
		result.put("price", price);
		result.put("sku",sku);
		result.put("picture",picture);
		result.put("category",category);
		result.put("deleted", deleted);

		return result;
	}
}