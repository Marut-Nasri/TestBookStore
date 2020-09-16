package com.example.BookStore.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.BookStore.Entity.Book;
import com.example.BookStore.Entity.BookPrice;
import com.example.BookStore.Entity.Order;
import com.example.BookStore.Entity.StoreUserDetails;
import com.example.BookStore.Entity.User;

@RestController
public class OrderController {
	@Autowired
	private OrderService orderService;

	public List<Order> findAll() {
		return orderService.findAll();
	}

	public List<Order> findByUserId(int userId) 
	{		
		return orderService.findByUserId(userId);
	}
	
	@Transactional
	@PostMapping(value="/users/order")
	public BookPrice createBookOrder(int[] bookIds) 
	{
		return orderService.createBookOrder(bookIds);
	}

}
