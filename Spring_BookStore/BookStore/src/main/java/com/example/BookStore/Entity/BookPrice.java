package com.example.BookStore.Entity;

public class BookPrice 
{
	private double price;
	
	public BookPrice() {}
	
	public BookPrice(double price)
	{
		this.setPrice(price);
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
