package com.nnn.repositories;

import org.springframework.data.repository.CrudRepository;

import com.nnn.objects.User;

public interface UserRepository extends CrudRepository<User, String>{

}
