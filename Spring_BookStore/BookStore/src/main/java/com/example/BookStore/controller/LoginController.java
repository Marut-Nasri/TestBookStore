package com.example.BookStore.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.servlet.ModelAndView;

import com.example.BookStore.Entity.User;
import com.example.BookStore.Entity.BookPrice;
import com.example.BookStore.Entity.StoreUserDetails;

@SessionAttributes({"currentUser"})
@RestController
public class LoginController {
    
	@GetMapping(value = "/logout")
    public ModelAndView logout(SessionStatus session) {

        SecurityContextHolder.getContext().setAuthentication(null);
        session.setComplete();

        ModelAndView model = new ModelAndView();
        model.setViewName("home");
		return model;
    }
	
    @PostMapping(value = "/login")
    public ResponseEntity<?> postLogin(Model model, HttpSession session) {

        // read principal out of security context and set it to session

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        validatePrinciple(authentication.getPrincipal());

        User loggedInUser = ((StoreUserDetails) authentication.getPrincipal()).getUserDetails();

        model.addAttribute("currentUser", loggedInUser.getUserName());
        model.addAttribute("CurrentUserId", loggedInUser.getUserId());
        
        session.setAttribute("CurrentUserId", loggedInUser.getUserId());

        return new ResponseEntity<String>("",HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/login")
	public ModelAndView loginPage(@RequestParam(value = "error",required = false) String error,
	@RequestParam(value = "logout",	required = false) String logout) {
		
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid Credentials provided.");
		}

		if (logout != null) {
			model.addObject("message", "Logged out from Book Store successfully.");
		}

		model.setViewName("login");
		return model;
	}

    private void validatePrinciple(Object principal) {
        if (!(principal instanceof StoreUserDetails)) {

            throw new  IllegalArgumentException("Principal can not be null!");

        }
    }
}
