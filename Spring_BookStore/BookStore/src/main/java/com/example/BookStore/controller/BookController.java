package com.example.BookStore.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.BookStore.Core.DataUtil;
import com.example.BookStore.Entity.Book;

@RestController
public class BookController {

	@Autowired
	private BookService bookService;

	@Transactional
	@GetMapping(value = "/books")
	public List<Book> getAllBooks() 
	{
		return bookService.findAll();
    }
		
	public Book findById(int id) {
		return bookService.findById(id);
	}
}
