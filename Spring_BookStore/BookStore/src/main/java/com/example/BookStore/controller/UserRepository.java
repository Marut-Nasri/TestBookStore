package com.example.BookStore.controller;

import java.util.*;
import java.time.*;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;

import com.example.BookStore.Entity.*;

public interface UserRepository extends MongoRepository<User, Integer>  
{
	List<User> findAll();
	User save(User user);			
}
