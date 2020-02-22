package com.cloudstorage.service;


import com.cloudstorage.model.Users;
import com.cloudstorage.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class SignUpService {


	private BCryptPasswordEncoder bcrypt;

	private UsersRepository usersRepository;

	@Autowired
	public SignUpService(BCryptPasswordEncoder bcrypt, UsersRepository usersRepository){
		this.bcrypt = bcrypt;
		this.usersRepository = usersRepository;
	}

	public void signUpUser(Users user){
		user.setPassword(bcrypt.encode(user.getPassword()));
		user.setDirectoryName(user.getUsername() + File.separator);

		usersRepository.save(user);
	}

	public void activateUser(Users user){
		user.setEnabled(true);
		usersRepository.save(user);
	}


	/*
	*  Under implementation, trying to figure out how to put this method under  */

	public void activateUser(String pinNumber){
		Optional<Users> user = Optional.ofNullable(findByPin(pinNumber));

		user.ifPresentOrElse(userEnable -> {
					userEnable.setEnabled(true);
					usersRepository.save(userEnable);

		}, () -> {  });
	}


	public List<Users> getAllUsers(){
		return usersRepository.findAll();
	}

	public Users findByUsername(String username){
		return usersRepository.findByUsername(username);
	}

	public Users findByEmail(String email){
		return usersRepository.findByEmail(email);
	}

	public Users findByPin(String pin){
		return usersRepository.findByPin(pin);
	}




	public int generateActivationPin(){
		return 10000 + new Random().nextInt(90000);
	}








}
