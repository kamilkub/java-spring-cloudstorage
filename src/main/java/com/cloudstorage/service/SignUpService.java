package com.cloudstorage.service;


import com.cloudstorage.model.Users;
import com.cloudstorage.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
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


	/*
	*  Prevents useless mongo connections */

	public boolean activateUser(String pinNumber){
		Optional<Users> user = Optional.ofNullable(findByPin(pinNumber));

		if(user.isPresent()){
		    Users activatedUser = user.get();
		    if(!activatedUser.isEnabled()){
				activatedUser.setEnabled(true);
				usersRepository.save(activatedUser);
			}

		    return true;
        }

		return false;

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
