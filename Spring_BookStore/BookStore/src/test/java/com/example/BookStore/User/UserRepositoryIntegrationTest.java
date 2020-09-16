package com.example.BookStore.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.BookStore.controller.UserService;

@SpringBootTest
public class UserRepositoryIntegrationTest {
	@Autowired
	private UserService userService;
	
	@Test
	public void TestRun_User() throws Exception 
	{
		String username = "";
		String password = "";
		String date_of_birth = "";

		var oldallUser = userService.findAll();

		String createResult = userService.CreateUser(username, password, date_of_birth);

		Assert.isTrue(createResult.equalsIgnoreCase("username must not empty."), "username must not empty.");

		username = "john.doe";

		createResult = userService.CreateUser(username, password, date_of_birth);

		Assert.isTrue(createResult.equalsIgnoreCase("password must not empty."), "password must not empty.");

		password = "thisismysecret";

		createResult = userService.CreateUser(username, password, date_of_birth);

		Assert.isTrue(createResult.equalsIgnoreCase("date_of_birth is invalid date"), "date_of_birth is invalid date");
		
		if (oldallUser.size() > 0  && 
			oldallUser.stream().filter(s -> s.getUserName().equalsIgnoreCase("john.doe")).findFirst() == null) {

			date_of_birth = "15/01/1985";
			
			createResult = userService.CreateUser(username, password, date_of_birth);
			Assert.isTrue(createResult.equalsIgnoreCase("Create User complete <a href='/home'>Home<a>") , "Create User complete");

			var allUser = userService.findAll();

			Assert.isTrue(allUser.size() - oldallUser.size() == 1, "Increase User member");

			var user = allUser.get(allUser.size() - 1);

			Assert.isTrue(user.getUserName().equals(username), "has user in list");
		}
				
	}
}
