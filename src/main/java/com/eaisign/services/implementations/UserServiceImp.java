package com.eaisign.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaisign.exceptions.UserNotFoundException;
import com.eaisign.models.User;
import com.eaisign.repository.UserRepository;

@Service
public class UserServiceImp {
	@Autowired
	UserRepository userRepository;
	
	public User findUser(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id).orElse(null);
		if(user==null) {
			throw new UserNotFoundException();
		}else {
			return user;
		}
	}
}
