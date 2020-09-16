package com.example.BookStore.Entity;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class Book 
{
	@Id  @JsonIgnore 
	private int bookId;
	private int id;
	private String name;
	private String author;
	private double price;
	private boolean is_recommended;	
	
	public Book(int bookId,int id,String name,String author,double price,boolean is_recommended) 
	{
		this.bookId = bookId;
		this.id = id;
		this.name = name;
		this.author = author;
		this.price = price;
		this.is_recommended = is_recommended;					
	}
	
	public int getBookId() {
		return this.bookId;
	}
	
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public double getPrice() {
		return price;
	}
	
	public Boolean getIs_recommended() {
		return is_recommended;
	}		
}
