package com.tutorial.service.impl;

import java.util.Date;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.tutorial.dao.UserDao;
import com.tutorial.model.User;
import com.tutorial.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-spring-service.xml", "classpath:spring-persist.xml", "classpath:spring-mybatis.xml"})
public class UserServiceImplTest {

	@Autowired
	private UserService userService;
	
	private UserDao userDao;
	
	@Before
	public void before() {
		userDao = EasyMock.createMock(UserDao.class);
		
		ReflectionTestUtils.setField(userService, "userDao", userDao);
	}
	
	@Test
	public void testAdd() {
		EasyMock.expect(userDao.insert(EasyMock.anyObject(User.class))).andReturn(1);
		EasyMock.replay(userDao);
		
		User user =  new User("account", "password", "email", "tel", "im", 1, new Date(), new Date());
		userService.add(user);
		
		EasyMock.verify(userDao);
	}
}
