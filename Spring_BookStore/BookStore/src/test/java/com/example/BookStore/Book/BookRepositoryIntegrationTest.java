package com.example.BookStore.Book;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.example.BookStore.controller.BookRepository;
import com.example.BookStore.controller.BookService;


@SpringBootTest
public class BookRepositoryIntegrationTest {
	@Autowired
	private BookService bookService;
	
	@Test
	public void TestRun_Book() throws Exception {
		var allBooks = bookService.findAll();
		
		Assert.isTrue(allBooks.size() > 0, "store must have book");
		
		if (allBooks.size() > 0) {
			var book = allBooks.get(allBooks.size()/2);
			
			var findBook = bookService.findById(book.getId());
			
			if (findBook != null) {
				Assert.isTrue(book.getName().equals(findBook.getName()),"Book can find by book Id");
			}
		}
		
		URL url = new URL("http://localhost:8080/books");
		HttpURLConnection  con = (HttpURLConnection)url.openConnection();
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
		
		Assert.isTrue(IsOK,"Can get book from API");
		JSONObject jsObj = null;
		
		if (IsOK) {
			String name = null;
			JSONArray jsArray = new JSONArray(dataOutput);
			Assert.isTrue(jsArray.length() > 0 ,"has book");
			Assert.isTrue(jsArray.length() == allBooks.size() ,"book size is ok");
			HashSet<String> nameHash = new HashSet<String>();
			for(int i=0;i<jsArray.length();i++) {
				jsObj = jsArray.getJSONObject(i);
				name = jsObj.getString("name");
				Assert.isTrue(!nameHash.contains(name) ,"not duplicate book");
				if (!nameHash.contains(name)) nameHash.add(name);
			}
		}
	}
}
