package com.cloudstorage;

import com.cloudstorage.model.Users;
import com.cloudstorage.repository.UsersRepository;
import com.cloudstorage.service.SignUpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CloudstorageApplicationTests {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Autowired
	private SignUpService signUpService;



	@Test
	public void contextLoads() {

		Users newUser = new Users();

		newUser.setUsername("mate");
		newUser.setEmail("mate@o2.pl");
		newUser.setPin("6543342432");
		newUser.setPassword("123");

		signUpService.signUpUser(newUser);

	}

}
