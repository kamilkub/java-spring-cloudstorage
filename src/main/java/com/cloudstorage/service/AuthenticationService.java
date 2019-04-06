package com.cloudstorage.service;


import com.cloudstorage.model.Users;
import com.cloudstorage.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationService implements UserDetailsService {


	@Autowired
	private UsersRepository usersRepository;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Users user = usersRepository.findByUsername(username);

		if(user == null){
			throw new UsernameNotFoundException("No user found for this username");
		}

		if(!user.isActivated()){
			throw new UsernameNotFoundException("User is not activated");
		}


		List<SimpleGrantedAuthority> auths = Arrays.asList(new SimpleGrantedAuthority("user"));


		return new User(user.getUsername(), user.getPassword(), auths);
	}
}
