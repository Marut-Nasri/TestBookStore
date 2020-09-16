package com.example.BookStore.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.BookStore.Entity.Book;
import com.example.BookStore.Entity.BookPrice;
import com.example.BookStore.Entity.Order;
import com.example.BookStore.Entity.StoreUserDetails;
import com.example.BookStore.Entity.User;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public List<Order> findByUserId(int userId) {
		var objList = orderRepository.findAll().stream().filter(s -> s.getUserId() == userId).toArray();
		List<Order> orders = new ArrayList<Order>();
		for (int i = 0; i < objList.length; i++) {
			orders.add((Order) objList[i]);
		}

		return orders;
	}

	@Override
	public BookPrice createBookOrder(int[] bookIds) {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User user = ((StoreUserDetails) authentication.getPrincipal()).getUserDetails();
        
        if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
        
		var orders = orderRepository.findAll();
		int maxId = 0;
		if (orders.size() > 0) {
			maxId = orders.stream().mapToInt(s -> s.getOrderId()).max().getAsInt();
		}
		
		Order order = null;
		Book book = null;
		double price = 0;
		
		int userId = user.getUserId();
		
		for(int i = 0;i<bookIds.length;i++) 
		{
			book = bookRepository.findByBookId(bookIds[i]);
			if (book == null) {
				throw new IllegalArgumentException("Book not found");
			}
			
			price += book.getPrice();
			maxId = maxId + 1;
			order = new Order(maxId,userId,bookIds[i]);
			orderRepository.insert(order);
		}		
				
		price = Math.round(price*100)/100;
					
		BookPrice bookPrice = new BookPrice(price);
		return bookPrice;
	}

}
