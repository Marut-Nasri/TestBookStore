package com.example.BookStore.controller;

import java.util.List;

import com.example.BookStore.Entity.Book;

public interface BookService {
	List<Book> findAll();
    Book findById(int id);
}
