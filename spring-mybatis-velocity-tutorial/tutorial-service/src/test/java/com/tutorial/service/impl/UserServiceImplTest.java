package com.tutorial.service.impl;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tutorial.model.User;
import com.tutorial.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-spring-service.xml", "classpath:spring-persist.xml", "classpath:spring-mybatis.xml"})
public class UserServiceImplTest {

	@Autowired
	private UserService userService;
	
	@Test
	public void testAdd() {
		User user =  new User("account", "password", "email", "tel", "im", 1, new Date(), new Date());
		userService.add(user);
	}
}
