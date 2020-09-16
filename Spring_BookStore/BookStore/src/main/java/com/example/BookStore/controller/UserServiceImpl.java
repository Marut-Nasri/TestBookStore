package com.example.BookStore.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.SessionStatus;

import com.example.BookStore.Core.DataUtil;
import com.example.BookStore.Entity.StoreUserDetails;
import com.example.BookStore.Entity.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public List<User> findAll() {		
		return userRepository.findAll();
	}

	@Override
	public User save(User user) {		
		return userRepository.save(user);
	}

	@Override
	public String CreateUser(String username, String password, String date_of_birth) {
		if (username == null || username.equals("")) {
			return "username must not empty.";
		}

		if (password == null || password.equals("")) {
			return "password must not empty.";
		}
		String name = "";
		String surname = "";
		int maxId = 0;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			formatter.localizedBy(Locale.ENGLISH);
			LocalDate birth_day = LocalDate.MIN;
			
			try 
			{						
				birth_day = LocalDate.parse(date_of_birth, formatter);
			}
			catch(Throwable t) {
				formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				formatter.localizedBy(Locale.ENGLISH);
				birth_day = LocalDate.parse(date_of_birth, formatter);
			}

			LocalDate now = LocalDate.now();

			if (birth_day.compareTo(now) > 0) {
				return "date_of_birth must not after today.";
			}

			formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			formatter.localizedBy(Locale.ENGLISH);

			date_of_birth = birth_day.format(formatter);
		} catch (Exception ex) {
			return "date_of_birth is invalid date";				
		}

		if (username != null && username.indexOf('.') >= 0) {
			name = username.substring(0, username.indexOf('.'));
			surname = username.substring(username.indexOf('.'));
		} else {
			name = username;
		}

		password = DataUtil.PasswordEncoder.encode(password);

		var alluser = userRepository.findAll();

		if (alluser.size() > 0) {			
			if (alluser.stream().filter(s->s.getUserName().equalsIgnoreCase(username)).findFirst() != null)
			{
				return  "UserName '" + username + "' is duplicate";					
			}

			maxId = alluser.stream().mapToInt(s -> s.getUserId()).max().getAsInt();
		}

		maxId = maxId + 1;

		User user = new User(maxId, username, name, surname, password, date_of_birth);
		userRepository.save(user);

		return "Create User complete <a href='/home'>Home<a>";	
	}

	@Override
	public ResponseEntity<User> getUserLogin() {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

		User user = ((StoreUserDetails) authentication.getPrincipal()).getUserDetails();

		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		int[] bookIds = orderRepository.findAll().stream().filter(s -> s.getUserId() == user.getUserId())
				.mapToInt(s -> s.getBookId()).toArray();
		
		user.setBookIds(bookIds);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> deleteCurrentUser(SessionStatus session) {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        User user = ((StoreUserDetails) authentication.getPrincipal()).getUserDetails();
                      
        if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
        
        String username = user.getUserName();
        
        var orderList = orderRepository.findByUserId(user.getUserId());
        
        if (orderList.size() > 0) 
        {
        	orderRepository.deleteAll(orderList);
        }
        
		userRepository.delete(user);
		
		SecurityContextHolder.getContext().setAuthentication(null);
	    if (session != null) session.setComplete();

        String message = "Delete User '"+username+"' Complete <p><a href='/home'>Home</a> </p>";
        
        return new ResponseEntity<String>(message,HttpStatus.OK);
	}
}
