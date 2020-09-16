package com.example.BookStore.controller;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.BookStore.Entity.*;

public interface OrderRepository extends MongoRepository<Order, Integer>  
{
	List<Order> findAll();
	List<Order> findByUserId(int userId);
}
