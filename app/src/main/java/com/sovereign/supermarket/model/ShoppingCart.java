package com.sovereign.supermarket.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

	private String sku;
	private String name;
	private String description;
	private double price;
	private String category;
	private Integer quantity;

	public ShoppingCart() {
	}

	public ShoppingCart(String sku, String name, String description, double price, Integer quantity) {
		this.sku = sku;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

		ShoppingCart item = (ShoppingCart) o;

		return sku.equals(item.sku);

	}

	@Override
	public int hashCode() {
		return sku.hashCode();
	}

	@Override
	public String toString() {
		return "Shopping Cart{" +
				"sku='" + sku + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", price=" + price +
				", quantity='" + quantity + '\'' +
				'}';
	}

	public ShoppingCart(String name, String description, Double price, Integer quantity) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity=quantity;
	}
	@Exclude
	public Map<String, Object> toMap() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("name", name);
		result.put("description", description);
		result.put("price", price);
		result.put("quantity", quantity);
		result.put("sku", sku);

		return result;
	}
}