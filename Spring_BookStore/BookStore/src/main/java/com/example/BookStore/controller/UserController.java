package com.example.BookStore.controller;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.session.Session;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.example.BookStore.Core.DataUtil;
import com.example.BookStore.Entity.*;
import com.mongodb.internal.session.SessionContext;

import io.netty.handler.codec.http.HttpResponseStatus;

@RestController
public class UserController {
	
	@Autowired
	private UserService userSerivce;
	
	public List<User> findAll(){
		return userSerivce.findAll();
	}
		
	public User save(User user) {
		return userSerivce.save(user);		
	}
	
	@Transactional
	@PostMapping(value = "/users")	
	public String CreateUser(String username, String password, String date_of_birth) {		
		return userSerivce.CreateUser(username, password, date_of_birth);		
	}
	
	@Transactional(readOnly = true)
	@GetMapping(value = "/users")
	public ResponseEntity<User> getUserLogin() 
	{
		return userSerivce.getUserLogin();
	}	
	
	@Transactional
	@DeleteMapping(value = "/users")
	public ResponseEntity<String> deleteCurrentUser(SessionStatus session) {
		return userSerivce.deleteCurrentUser(session);		
	}			
}
