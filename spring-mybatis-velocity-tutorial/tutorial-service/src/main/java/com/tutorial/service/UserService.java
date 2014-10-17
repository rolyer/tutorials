package com.tutorial.service;

import java.util.List;

import com.tutorial.model.User;

public interface UserService {
	public void add(User user);
	
	public User queryUserById(Integer id);
	
	public List<User> query();
}
