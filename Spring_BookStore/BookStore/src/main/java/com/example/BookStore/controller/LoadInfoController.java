package com.example.BookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookStore.Entity.LoadInfo;

@RestController
public class LoadInfoController {
	@Autowired
	private LoadInfoRepository loadinfoRepository;
	
	public List<LoadInfo> findAll(){
		return loadinfoRepository.findAll();
	}
}
