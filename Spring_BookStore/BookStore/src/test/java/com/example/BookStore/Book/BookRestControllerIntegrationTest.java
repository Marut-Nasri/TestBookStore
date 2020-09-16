package com.example.BookStore.Book;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.*;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import com.example.BookStore.controller.BookController;
import com.example.BookStore.controller.BookRepository;

import reactor.netty.http.client.HttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import javax.net.ssl.HttpsURLConnection;
import javax.xml.ws.spi.http.HttpContext;

@RunWith(SpringRunner.class)
public class BookRestControllerIntegrationTest {
	
    @Autowired
    private MockMvc mvc;
    
    @Test
	public void givenBook_whenfindAll_thenReturnJsonArray() throws Exception
	{    	
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
