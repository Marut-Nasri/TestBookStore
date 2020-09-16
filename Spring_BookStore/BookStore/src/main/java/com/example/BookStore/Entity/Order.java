package com.example.BookStore.Entity;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Order {
	@Id  @JsonIgnore 
	private int orderId;
	private int userId;
	private int bookId;
	
	public Order(int orderId,int userId,int bookId) {
		this.orderId = orderId;
		this.setUserId(userId);
		this.setBookId(bookId);
	}

	public int getOrderId() {
		return orderId;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
}
