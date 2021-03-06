package com.example.BookStore.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookStore.Core.DataUtil;
import com.example.BookStore.Entity.Book;

@Service
public class BookServiceImpl implements BookService {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private LoadInfoRepository loadinfoRepository;
	
	@Override
	public List<Book> findAll() {
		var loadConfig = loadinfoRepository.findAll().stream().findFirst().get();
		LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); 
        formatter.localizedBy(Locale.ENGLISH);
        LocalDate lastUpdate = LocalDate.parse(loadConfig.getLastUpdate(), formatter);
        
        if (now.compareTo(lastUpdate) > 0) 
        {        	        
        	try 
        	{
        		var allBooks = DataUtil.GetAllBook(loadConfig);
        		String lastUpdateStr = now.format(formatter);
        		loadConfig.setLastUpdate(lastUpdateStr);
        		loadinfoRepository.save(loadConfig);
        		bookRepository.deleteAll();        		
        		bookRepository.saveAll(allBooks);        		
        	}
        	catch(Throwable th) {
        		return null;        		
        	}
        }
        
        return bookRepository.findAll();
	}

	@Override
	public Book findById(int id) {
		var allBooks = bookRepository.findAll();
		if (allBooks.size() == 0 || allBooks.stream().filter(s->s.getId() == id).findFirst() == null) 
		{
			return null;
		}
		Book book = allBooks.stream().filter(s->s.getId() == id).findFirst().get();
		return book;
	}

}
