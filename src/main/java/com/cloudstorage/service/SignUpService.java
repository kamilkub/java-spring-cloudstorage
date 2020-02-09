package com.cloudstorage.service;


import com.cloudstorage.model.Users;
import com.cloudstorage.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class SignUpService {


    @Autowired
	private BCryptPasswordEncoder bcrypt;

	private UsersRepository usersRepository;

	@Autowired
	public SignUpService(UsersRepository usersRepository){
		this.usersRepository = usersRepository;
	}

	public void signUpUser(Users user){
		user.setPassword(bcrypt.encode(user.getPassword()));
		usersRepository.save(user);
	}

	public Users activateUser(Users user){
		user.setEnabled(true);
		return usersRepository.save(user);
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
