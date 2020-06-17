package com.cloudstorage;

import com.cloudstorage.model.FileObject;
import com.cloudstorage.model.StorageUser;
import com.cloudstorage.repository.UsersRepository;
import com.cloudstorage.service.SignUpService;
import com.cloudstorage.service.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CloudstorageApplicationTests {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Autowired
	private SignUpService signUpService;

	@Autowired
	private StorageService storageService;



	@Test
	public void contextLoads() {
		StorageUser newUser = new StorageUser();
		newUser.setUsername("mate");
		newUser.setEmail("mate@o2.pl");
		newUser.setPin("6543342432");
		newUser.setPassword("123");

		signUpService.signUpUser(newUser);

	}

	@Test
	public void testGetAllDirectoriesAndFilesMethodAndIsBaseFileConstructorWorkingProperly() {
		ArrayList<FileObject> fileAndDirectoriesPaths = storageService.getFileAndDirectoriesPaths("kamkk");
		fileAndDirectoriesPaths.forEach(element -> System.out.println(element.getFileName() + " " + element.getFullPath()));

	}

	@Test
	public void testValueOfBasePathInStorageServiceClass() {
		assertEquals("Is not equal","/cloudstorage/" , storageService.getBasePathStorage());
	}



}
