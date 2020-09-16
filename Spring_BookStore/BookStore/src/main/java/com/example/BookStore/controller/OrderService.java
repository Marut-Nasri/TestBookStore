package com.example.BookStore.controller;

import java.util.List;

import com.example.BookStore.Entity.BookPrice;
import com.example.BookStore.Entity.Order;

public interface OrderService {
	List<Order> findAll();
	List<Order> findByUserId(int userId);
	BookPrice createBookOrder(int[] bookIds);
}
