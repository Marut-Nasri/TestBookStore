package com.example.BookStore.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;
import com.example.BookStore.Entity.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
	@Autowired
    private UserService userService;
	
	
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userService.findAll().stream().filter(s->s.getUserName().equalsIgnoreCase(username)).findFirst().get();

        if (user == null) {

            throw new UsernameNotFoundException("User not found.");
        }

        return new StoreUserDetails(user);
    }    
}
