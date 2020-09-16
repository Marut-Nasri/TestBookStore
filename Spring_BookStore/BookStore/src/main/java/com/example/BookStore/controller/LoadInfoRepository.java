package com.example.BookStore.controller;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.BookStore.Entity.Book;
import com.example.BookStore.Entity.LoadInfo;

public interface LoadInfoRepository extends MongoRepository<LoadInfo, Integer>{
	List<LoadInfo> findAll();
}
