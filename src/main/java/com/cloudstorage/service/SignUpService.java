package com.cloudstorage.service;


import com.cloudstorage.model.Users;
import com.cloudstorage.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignUpService {


    @Autowired
	private BCryptPasswordEncoder bcrypt;

	@Autowired
	private UsersRepository usersRepository;

	public SignUpService(UsersRepository usersRepository){
		this.usersRepository = usersRepository;
	}

	public Users signUpUser(Users user){
		user.setPassword(bcrypt.encode(user.getPassword()));
		return usersRepository.save(user);
	}

	public Users activateUser(Users user){
		user.setActivated(true);
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




	private String generatePIN(){
		return "";
	}








}
