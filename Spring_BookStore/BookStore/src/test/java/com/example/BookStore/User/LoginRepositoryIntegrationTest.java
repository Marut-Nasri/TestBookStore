package com.example.BookStore.User;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.Assert;

import com.example.BookStore.Entity.StoreUserDetails;
import com.example.BookStore.Entity.User;
import com.example.BookStore.controller.BookRepository;
import com.example.BookStore.controller.OrderRepository;
import com.example.BookStore.controller.OrderService;
import com.example.BookStore.controller.UserService;

@SpringBootTest
public class LoginRepositoryIntegrationTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private BookRepository bookRespository;
	
	@Test
    public void TestLoginUser() throws Exception {
        var allUser = userService.findAll();
        String username = "";
		String password = "";
		String date_of_birth = "";
		
        if (allUser.size() == 0 || allUser.stream().filter(s -> s.getUserName().equalsIgnoreCase("john.doe")).findFirst() == null) {
        	username = "john.doe";
        	password = "thisismysecret";
        	date_of_birth = "15/01/1985";
        	String createResult = userService.CreateUser(username, password, date_of_birth);
			Assert.isTrue(createResult.equalsIgnoreCase("Create User complete <a href='/home'>Home<a>") , "Create User complete");
        }
        
        allUser = userService.findAll();
        User user = allUser.stream().filter(s -> s.getUserName().equalsIgnoreCase("john.doe")).findFirst().get();
        StoreUserDetails storeUser = new StoreUserDetails(user);
        
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(storeUser,null));
        SecurityContextHolder.setContext(securityContext);
        
        
        var userResponse = userService.getUserLogin();
        
        Assert.isTrue(userResponse.getBody().getUserName().equals("john.doe"),"fake login success");
        
        int[] bookIds = new int[] {1,2,3,4,5};
                
        var bookPrice =  orderService.createBookOrder(bookIds);
        
        var userLogin = userService.getUserLogin().getBody();
        
        Assert.isTrue(userLogin.getBookIds().length >= bookIds.length,"Order confirm");
        
        var allBooks = bookRespository.findAll();
        List<Integer> bookIdList = new ArrayList<Integer>();
        for(int i =0;i<bookIds.length;i++) {
        	bookIdList.add(bookIds[i]);
        }                
        double price = 0;
        for(int i = 0;i<allBooks.size();i++) 
        {
        	for(int j = 0 ;j < bookIds.length;j++) {
        		if (allBooks.get(i).getBookId() == bookIds[j]) 
        		{
        			price += allBooks.get(i).getPrice();
        			break;
        		}
        	}
        }
        
        Assert.isTrue(bookPrice.getPrice() == Math.round(price*100)/100,"Price confirm");
        
        int userId = userLogin.getUserId();
        
        userService.deleteCurrentUser(null);
        
        allUser = userService.findAll();
        
        user = null;
        
        if (allUser.size() > 0 && allUser.stream().filter(s -> s.getUserName().equalsIgnoreCase("john.doe")).findFirst() != null) {
        	user = allUser.stream().filter(s -> s.getUserName().equalsIgnoreCase("john.doe")).findFirst().get();
        }
                        
        var orderList = orderService.findByUserId(userId);
        
        Assert.isTrue(user == null,"delete user complete");
        Assert.isTrue(orderList.size() == 0,"delete order complete");
    }
	
	
}
