package com.ashu.rms.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ashu.rms.api.common.model.User;
import com.ashu.rms.api.service.UserService;

@RestController
public class LoginController {
	private Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;
	
	@RequestMapping(path="/getUser/{username}/{password}", method=RequestMethod.GET)
	public User getUser(@PathVariable("username") String username, @PathVariable("password") String password){
		logger.info("getUserCalled");
		System.out.println(username + password);
		return userService.getUser(username, password);
	}
	
	@RequestMapping(path="/test}", method=RequestMethod.GET)
	public User test(String password){
		return null;
	}
}
