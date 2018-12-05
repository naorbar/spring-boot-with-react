package com.nnn.controllers;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.nnn.managers.HrManager;
import com.nnn.objects.User;

@RestController
public class HrController {

	@Autowired
	private HrManager hrManager;
	
	@GetMapping(path = "/test")
	public String test() {
		return "HELLO, The server time is " + new Date();
	}
	
	@GetMapping(path = "/users_static")
	public Iterable<User> getUsersStaticList() {
		return hrManager.getAllUsersStatic();
	}
	
	@GetMapping(path = "/users")
	public Iterable<User> getUsers() {
		return hrManager.getAllUsers();
	}
	
	@GetMapping(path = "/users/{id}")
	public Optional<User> getUserById(@PathVariable String id) {
		return hrManager.getUserById(id);
	}
}
