package com.tutorial.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tutorial.dao.UserDao;
import com.tutorial.model.User;
import com.tutorial.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public void add(User user) {
		userDao.insert(user);
	}

	@Override
	public User queryUserById(Integer id) {
		return userDao.queryUserById(id);
	}

	@Override
	public List<User> query() {
		return userDao.query();
	}

}
