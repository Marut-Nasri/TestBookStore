package com.example.BookStore.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CustomErrorController implements ErrorController {

	@RequestMapping(value="/error")
	public ModelAndView handleError(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		model.setViewName("error");
		
		String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
		Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
		String exception = (String) request.getAttribute("javax.servlet.error.exception");

		model.getModelMap().put("ERROR_REQUEST_URI", requestUri);
		model.getModelMap().put("ERROR_STATUS_CODE", code);
		model.getModelMap().put("ERROR_MESSAGE", errorMessage);
		model.getModelMap().put("exception", exception);

		return model;
	}
	
	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return "/error";
	}

}
