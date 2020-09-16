package com.example.BookStore.controller;

import com.example.BookStore.Entity.*;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, Integer> 
{
    List<Book> findAll();
    Book findById(int id);
}