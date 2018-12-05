package com.nnn.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nnn.objects.User;
import com.nnn.repositories.UserRepository;

@Component
public class HrManager {

	@Autowired
	private UserRepository userRepository;
	
	public List<User> getAllUsersStatic() {
		List<User> list = new ArrayList<User>();
		list.add(new User.Builder().withId("Naor").withName("Naor Bar").withTitle("").build());
		list.add(new User.Builder().withId("Naor2").withName("Naor Bar 2").withTitle("TL").build());
		
		return list;
	}
	
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
	}
}
