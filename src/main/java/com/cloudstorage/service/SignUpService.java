package com.cloudstorage.service;


import com.cloudstorage.model.StorageUser;
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


	public String signUpUser(StorageUser user){
		String stringPin = String.valueOf(generateActivationPin());

		user.setPersisted(true);
		user.setPassword(bcrypt.encode(user.getPassword()));
		user.setDirectoryName(user.getUsername() + File.separator);
		user.setPin(stringPin);

		usersRepository.save(user);

		return stringPin;
	}


	/*
	*  Prevents useless mongo connections */

	public boolean activateUser(String pinNumber){
		Optional<StorageUser> user = Optional.ofNullable(findByPin(pinNumber));

		if(user.isPresent()){
		    StorageUser activatedUser = user.get();
		    if(!activatedUser.isEnabled()){
				activatedUser.setEnabled(true);
				usersRepository.save(activatedUser);
			}

		    return true;
        }

		return false;

	}


	public List<StorageUser> getAllUsers(){
		return usersRepository.findAll();
	}

	public StorageUser findByUsername(String username){
		return usersRepository.findByUsername(username);
	}

	public StorageUser findByEmail(String email){
		return usersRepository.findByEmail(email);
	}

	public boolean isEmailTaken(String email){
		Optional<StorageUser> isEmailTaken = Optional.ofNullable(usersRepository.findByEmail(email));
		return isEmailTaken.isPresent();
	}

	public boolean isUsernameTaken(String username){
		Optional<StorageUser> isUsernameTaken = Optional.ofNullable(usersRepository.findByUsername(username));
		return isUsernameTaken.isPresent();
	}


	public StorageUser findByPin(String pin){
		return usersRepository.findByPin(pin);
	}

	public int generateActivationPin(){
		return 10000 + new Random().nextInt(90000);
	}



}
