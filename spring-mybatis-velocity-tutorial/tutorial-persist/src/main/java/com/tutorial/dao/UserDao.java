package com.tutorial.dao;

import java.util.List;

import com.tutorial.model.User;

public interface UserDao {
	public int insert(User user);
	
//	@Cacheable(value= "mycache")
	public User queryUserById(Integer id);
	
//	@Cacheable(value= "mycache")
	public List<User> query();
}
