package com.example.librarySystem.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.librarySystem.domain.model.User;
import com.example.librarySystem.domain.repository.UserRepository;


@Service
public class SuperUserDetailsService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username + "is not found"));
		return new SuperUserDetails(user);
	}
	
	public void userregist(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
	}
	
	public List<User> getUserAll(){
		return userRepository.findAll();
	}
	
	public User findById(String userId) {
		return userRepository.findById(userId).get();
	}
	
	public boolean checkId(String userId) {
		return userRepository.existsById(userId);
	}

}
