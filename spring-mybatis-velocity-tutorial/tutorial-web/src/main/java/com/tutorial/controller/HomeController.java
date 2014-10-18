package com.tutorial.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tutorial.dto.Result;
import com.tutorial.model.User;
import com.tutorial.service.UserService;

@Controller
@RequestMapping("/")
public class HomeController {
	@Autowired
	private UserService userService;

	@RequestMapping("index.html")
	public void index(ModelMap out) {
		out.put("message", "User List:");
	}

	@RequestMapping(value="add.html", method=RequestMethod.POST)
	public @ResponseBody Result add(String account, String password) {
		Result result = new Result();

		User user = new User(account, password, new Date(), new Date());
		userService.add(user);

		result.setSuccess(true);
		return result;
	}
	
	@RequestMapping(value="load.html", method=RequestMethod.POST)
	public @ResponseBody Result load() {
		Result result = new Result();
		
		result.setData(userService.query());
		result.setSuccess(true);
		
		return result;
	}

}
