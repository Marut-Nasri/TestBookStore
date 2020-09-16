package com.example.BookStore.Entity;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.*;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.Data;

@Data
@Document
public class User {
	@Id  @JsonIgnore 
	private int userId;
	
	@JsonIgnore 
	private String userName;
	private String name;
	private String surname;
	@JsonIgnore 
	private String password;
	private String date_of_birth;	
	@Transient 
	private int[] bookIds;
		
	public User() {
	}
	
	public User(int userId,String userName,String name,String surname,String password,String date_of_birth) {		
		this.userId = userId;
		this.userName = userName;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.date_of_birth = date_of_birth;
	}
	
	public void setBookIds(int[] bookIds) {
		this.bookIds = bookIds;
	}
	
	public int[] getBookIds() {
		return this.bookIds;
	}
		
	public int getUserId() {
		return this.userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
