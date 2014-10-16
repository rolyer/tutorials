package com.tutorial.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tutorial.model.User;
import com.tutorial.service.UserService;

@Controller
@RequestMapping("/")
public class HomeController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("index.html")
	public void index(ModelMap out){
		User user =  new User("account", "password", "email", "tel", "im", 1, new Date(), new Date());
		userService.add(user);
		
		out.put("message", "Hello World!");
	}
}
