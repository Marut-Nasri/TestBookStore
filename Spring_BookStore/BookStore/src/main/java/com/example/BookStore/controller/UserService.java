package com.example.BookStore.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.support.SessionStatus;

import com.example.BookStore.Entity.User;

public interface UserService {
	List<User> findAll();
	User save(User user);
	String CreateUser(String username, String password, String date_of_birth);
	ResponseEntity<User> getUserLogin();
	ResponseEntity<String> deleteCurrentUser(SessionStatus session);
}
