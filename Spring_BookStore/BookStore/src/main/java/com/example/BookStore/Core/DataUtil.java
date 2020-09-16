package com.example.BookStore.Core;

import java.io.*;

import java.net.*;
import java.net.http.*;
import java.time.LocalDate;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.boot.configurationprocessor.json.*;
import org.springframework.context.*;
import org.springframework.security.crypto.bcrypt.*;
import com.example.BookStore.Entity.*;

public class DataUtil 
{
	public static String DefaultRecommendedBookUrl = "https://scb-test-book-publisher.herokuapp.com/books/recommendation";
	public static String DefaultAllBookUrl = "https://scb-test-book-publisher.herokuapp.com/books";
	
	public static LoadInfo LastUpdate = null;
	public static BCryptPasswordEncoder PasswordEncoder = new BCryptPasswordEncoder();

	public static JSONArray GetJsonFromRequest(String apiURL,int requestTimeout) throws Throwable
	{
		URL url = new URL(apiURL);
		HttpsURLConnection  con = (HttpsURLConnection)url.openConnection();
		int status = con.getResponseCode();
		boolean IsOK = false;				
		
		Reader streamReader = null;
		 
		if (status > 299) {
		    streamReader = new InputStreamReader(con.getErrorStream());
		} else {
			IsOK = true;
		    streamReader = new InputStreamReader(con.getInputStream());
		}
				
		BufferedReader in = new BufferedReader(streamReader);
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
		in.close();
		
		String dataOutput = content.toString();
		
		if (IsOK) {
			JSONArray jsArray = new JSONArray(dataOutput);
			return jsArray;
		}
		else {
			throw new ApplicationContextException(dataOutput);
		}
	}
	
	public static List<Book> GetAllBook(LoadInfo config)  throws Throwable
	{
		String recommendedBookUrl = config.getRecommendBookURI();
		String allBookUrl = config.getAllBookURI();
		int requestTimeout = config.getRequestTimeOut();

		
		if (recommendedBookUrl == null) recommendedBookUrl = DefaultRecommendedBookUrl;
		if (allBookUrl == null) allBookUrl = DefaultAllBookUrl;
		
		HashMap<String, Book> bookHash = new HashMap<String, Book>();
		JSONArray jsArrayRecBook = GetJsonFromRequest(recommendedBookUrl, requestTimeout);
		JSONArray jsArrayAllBook = GetJsonFromRequest(allBookUrl, requestTimeout);
		List<String> bookNames = new ArrayList<String>();
		JSONObject jsObj = null;
		Book book = null;
		int bookId = 0;
		int id = 0;
		String name = null;
		String author = null;
		double price = 0;
		List<Book> allBooks = new ArrayList<Book>();
		for (int i = 0; i < jsArrayRecBook.length(); i++) {
			jsObj = jsArrayRecBook.getJSONObject(i);
			
			id = jsObj.getInt("id");
			name = jsObj.getString("book_name");
			author = jsObj.getString("author_name");
			price = jsObj.getDouble("price");
			
			book = new Book(bookId,id, name, author, price, true);
			bookNames.add(name);
			bookHash.put(name, book);
		}

		var sortedName = bookNames.stream().sorted().toArray();

		bookId = 0;
		for (int i = 0; i < sortedName.length; i++) {
			bookId++;
			book = bookHash.get(sortedName[i].toString());
			book.setBookId(bookId);
			allBooks.add(book);
		}

		bookNames.clear();

		for (int i = 0; i < jsArrayAllBook.length(); i++) {
			jsObj = jsArrayAllBook.getJSONObject(i);
			name = jsObj.getString("book_name");

			if (bookHash.containsKey(name))
				continue;

			id = jsObj.getInt("id");
			author = jsObj.getString("author_name");
			price = jsObj.getDouble("price");
			
			book = new Book(bookId,id, name, author, price, false);
			bookNames.add(name);
			bookHash.put(name, book);
		}

		sortedName = bookNames.stream().sorted().toArray();

		for (int i = 0; i < sortedName.length; i++) {
			bookId++;
			book = bookHash.get(sortedName[i].toString());
			book.setBookId(bookId);
			allBooks.add(book);
		}

		return allBooks;
	}
	
}
