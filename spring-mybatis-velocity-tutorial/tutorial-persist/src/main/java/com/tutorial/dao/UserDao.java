package com.tutorial.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.tutorial.model.User;

public interface UserDao {
	public int insert(User user);
	
	@Cacheable(value = "user")
	public User queryUserById(Integer id);
	
	@Cacheable(value="users")
	public List<User> query();
}
